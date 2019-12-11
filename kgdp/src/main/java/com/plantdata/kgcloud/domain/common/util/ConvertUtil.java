package com.plantdata.kgcloud.domain.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.KgDocumentErrorCodes;
import com.plantdata.kgcloud.domain.common.entity.TitleBean;
import com.plantdata.kgcloud.domain.common.entity.TitleLevel;
import com.plantdata.kgcloud.domain.common.entity.WordContent;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.UUIDUtils;
import net.arnx.wmf2svg.gdi.svg.SvgGdi;
import net.arnx.wmf2svg.gdi.wmf.WmfParser;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class ConvertUtil {

    private final static List<String> titleStyleLsit = Lists.newArrayList("10","20","30","40","50","60","70","80","90");
    private final static String match = "^\\d[.、].*";

    public static byte[] emfToPng(byte[] is){
        byte[] by=null;
        EMFInputStream emf = null;
        EMFRenderer emfRenderer = null;
        //创建储存图片二进制流的输出流
        ByteArrayOutputStream baos = null;
        //创建ImageOutputStream流
        ImageOutputStream imageOutputStream = null;
        try {
            emf = new EMFInputStream(new ByteArrayInputStream(is), EMFInputStream.DEFAULT_VERSION);
            emfRenderer = new EMFRenderer(emf);

            int width = (int)(emf.readHeader().getBounds().getWidth());
            int height = (int)(emf.readHeader().getBounds().getHeight());
            if(width < 300){
                width = width + 300;
            }
            if(height < 300){
                height = height + 300;
            }
            final BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = result.createGraphics();
            emfRenderer.paint(g2);

            //创建储存图片二进制流的输出流
            baos = new ByteArrayOutputStream();
            //创建ImageOutputStream流
            imageOutputStream = ImageIO.createImageOutputStream(baos);
            //将二进制数据写进ByteArrayOutputStream
            ImageIO.write(result, "png", imageOutputStream);
            //inputStream = new ByteArrayInputStream(baos.toByteArray());
            by=baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{

            if(imageOutputStream!=null){
                try {
                imageOutputStream.close();
                } catch (IOException e) {
                }
            }
            if(baos!=null){
                IOUtils.closeQuietly(baos);
            }
            if(emfRenderer!=null){
                emfRenderer.closeFigure();
            }
            if(emf!=null){
                try {
                    emf.close();
                } catch (IOException e) {
                }
            }

        }
        return by;
    }

    public static byte[] wmfToPng(byte[] content,String documentPath) {
        String tempFile = documentPath+"/"+UUIDUtils.getShortString();
        String tempImage = documentPath+"/"+UUIDUtils.getShortString();
        boolean compatible = false;
        FileOutputStream out = null;
        FileOutputStream jpg = null;
        FileInputStream svgInputStream = null;
        FileInputStream imgInputStream = null;
        try(InputStream in = new ByteArrayInputStream(content);) {

            WmfParser parser = new WmfParser();
            final SvgGdi gdi = new SvgGdi(compatible);
            parser.parse(in, gdi);

            org.w3c.dom.Document doc = gdi.getDocument();
            out = output(doc,new FileOutputStream(tempFile));

            Float h = 300F;
            try {
                String height = doc.getDocumentElement().getAttribute("height");
                h = Float.parseFloat(height) / 100;
                if(h < 300){
                    h = 100F;
                }
            }catch (Exception e){
            }
            Float w = 300F;
            try {
                String widht = doc.getDocumentElement().getAttribute("width");
                w = Float.parseFloat(widht) / 100;
                if(w < 300){
                    w = 300F;
                }
            }catch (Exception e){
            }


            out.flush();

            ImageTranscoder it = new PNGTranscoder();
            it.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(1f));
            it.addTranscodingHint(ImageTranscoder.KEY_WIDTH, w);
            it.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, h);
//            it.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(0.9f));
            jpg = new FileOutputStream(tempImage);
            svgInputStream = new FileInputStream(tempFile);

            it.transcode(new TranscoderInput(svgInputStream),new TranscoderOutput(jpg));
            jpg.flush();

            imgInputStream = new FileInputStream(tempImage);
            return inputStream2byte(imgInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if(out != null){
                try{
                    out.close();
                }catch (Exception e){}
            }
            if(jpg != null){
                try{
                    jpg.close();
                }catch (Exception e){}
            }
            if(svgInputStream != null){
                try{
                    svgInputStream.close();
                }catch (Exception e){}
            }
            if(imgInputStream != null){
                try{
                    imgInputStream.close();
                }catch (Exception e){}
            }

        }
        return null;
    }


    public static byte[] inputStream2byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            byteArrayOutputStream.write(buff, 0, rc);
        }
        return byteArrayOutputStream.toByteArray();
    }


    private static FileOutputStream output(org.w3c.dom.Document doc, FileOutputStream out) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(out));
        return out;
    }

    public static TitleBean getTitle(HWPFDocument doc) {


        Range r = doc.getRange();

        List<TitleLevel> titleLevelList = Lists.newArrayList();
        Integer structure = 1;
        Map<String,String> map = Maps.newHashMap();
        for (int i = 0; i < r.numParagraphs(); i++) {

            String text = null;
            int lvl = 0;
            Paragraph p = r.getParagraph(i);

            int numStyles = doc.getStyleSheet().numStyles();
            int styleIndex = p.getStyleIndex();
            boolean isTitle = false;
            if (numStyles > styleIndex) {
                StyleSheet style_sheet = doc.getStyleSheet();
                StyleDescription style = style_sheet.getStyleDescription(styleIndex);
                String styleName = style.getName();
                if (styleName != null && styleName.contains("标题") && !p.text().trim().replaceAll("\\r","").equals("目录")) {
                    isTitle = true;
                    if(styleName.contains(",")){
                        String[] s = styleName.split(",");
                        for(int j=0; i<s.length; j++){
                            if(s[j].contains("标题")){
                                styleName = s[j];
                                break;
                            }
                        }
                    }


                    if(styleName.contains(" ") && styleName.split(" ").length > 1){
                        styleName = styleName.split(" ")[1];
                    }

                    try {
                        Integer level = Integer.parseInt(styleName);
                        TitleLevel titleLevel = new TitleLevel(p.text().trim().replaceAll("\\r","").replaceAll("\\n",""),level);
                        titleLevel.setSerialNumber(titleLevelList);
                        titleLevelList.add(titleLevel);
                    }catch (NumberFormatException e){
                        System.out.println("错误的标题格式："+styleName);
                    }
                }
            }
            if(!isTitle && (text == null || lvl == 0)){
                if(p.getLvl() < 9){
                    String t = p.text().trim().replaceAll("\\r","").replaceAll("\\n","");
                    if(t.isEmpty()){
                        continue;
                    }
                    TitleLevel titleLevel = new TitleLevel(p.text().trim().replaceAll("\\r","").replaceAll("\\n",""),p.getLvl()+1);

                    titleLevel.setSerialNumber(titleLevelList);
                    titleLevelList.add(titleLevel);
                }
            }
        }

        return TitleBean.builder().structure(structure).titleList(titleLevelList).build();
    }

    public static TitleBean getTitle(XWPFDocument doc) {

        List<TitleLevel> titleList = Lists.newArrayList();

        Integer structure = 1;
        for (int i = 0; i < doc.getParagraphs().size(); i++) {
            XWPFParagraph p = doc.getParagraphs().get(i);

            String style = p.getStyle();
            TitleLevel titleLevel = null;

            if(style != null && titleStyleLsit.contains(style)){
                switch (style){

                    case "10":
                        titleLevel = new TitleLevel(p.getParagraphText(),1);
                        break;
                    case "20":
                        titleLevel = new TitleLevel(p.getParagraphText(),2);
                        break;
                    case "30":
                        titleLevel = new TitleLevel(p.getParagraphText(),3);
                        break;
                    case "40":
                        titleLevel = new TitleLevel(p.getParagraphText(),4);
                        break;
                    case "50":
                        titleLevel = new TitleLevel(p.getParagraphText(),5);
                        break;
                    case "60":
                        titleLevel = new TitleLevel(p.getParagraphText(),6);
                        break;
                    case "70":
                        titleLevel = new TitleLevel(p.getParagraphText(),7);
                        break;
                    case "80":
                        titleLevel = new TitleLevel(p.getParagraphText(),8);
                        break;
                    case "90":
                        titleLevel = new TitleLevel(p.getParagraphText(),9);
                        break;
                    default:
                        break;
                }


            }else if(p.getCTP() != null && p.getCTP().getPPr() != null && p.getCTP().getPPr().getOutlineLvl() != null){

                titleLevel = new TitleLevel(p.getParagraphText(),p.getCTP().getPPr().getOutlineLvl().getVal().intValue()+1);
            }else if(doc.getStyles().getStyle(p.getStyleID()) != null
                    && doc.getStyles().getStyle(p.getStyleID()).getCTStyle() != null
                    && doc.getStyles().getStyle(p.getStyleID()).getCTStyle().getPPr() != null
                    && doc.getStyles().getStyle(p.getStyleID()).getCTStyle().getPPr().getOutlineLvl() != null){

                titleLevel = new TitleLevel(p.getParagraphText(),doc.getStyles().getStyle(p.getStyleID()).getCTStyle().getPPr().getOutlineLvl().getVal().intValue()+1);
            }else if(doc.getStyles().getStyle(p.getStyle()) != null
                    && doc.getStyles().getStyle(p.getStyle()).getCTStyle() != null
                    && doc.getStyles().getStyle(p.getStyle()).getCTStyle().getBasedOn() != null
                    && doc.getStyles().getStyle(doc.getStyles().getStyle(p.getStyle()).getCTStyle().getBasedOn().getVal()) != null
                    && doc.getStyles().getStyle(doc.getStyles().getStyle(p.getStyle()).getCTStyle().getBasedOn().getVal()).getCTStyle() != null
                    && doc.getStyles().getStyle(doc.getStyles().getStyle(p.getStyle()).getCTStyle().getBasedOn().getVal()).getCTStyle().getPPr() != null
                    && doc.getStyles().getStyle(doc.getStyles().getStyle(p.getStyle()).getCTStyle().getBasedOn().getVal()).getCTStyle().getPPr().getOutlineLvl() != null){

                titleLevel = new TitleLevel(p.getParagraphText(),doc.getStyles().getStyle(doc.getStyles().getStyle(p.getStyle()).getCTStyle().getBasedOn().getVal()).getCTStyle().getPPr().getOutlineLvl().getVal().intValue()+1);
            }

            if(titleLevel != null){
                titleLevel.setSerialNumber(titleList);
                titleList.add(titleLevel);
            }
        }

        return TitleBean.builder().structure(structure).titleList(titleList).build();
    }

    public static String getAll(BufferedReader reader) throws IOException {
        String line = "";
        while (true){
            String l  = reader.readLine();

            if(l == null){
                break;
            }

            line += l;

        }

        return line;
    }

    public static List<WordContent> setTitleAtt(String path, TitleBean titleBean){

        try(FileReader r = new FileReader(new File(path));BufferedReader reader = new BufferedReader(r)){

            String html = getAll(reader);

            org.jsoup.nodes.Document htmlDoc = Jsoup.parse(html);
            Elements imgElements = htmlDoc.body().select("img");
            for(int i=0; i< imgElements.size(); i++){

                //给emf wmf图片添加默认宽度 避免过大
                Element img = imgElements.get(i);
                if(img.attributes().get("src") != null && img.attributes().get("src").contains(".emf") || img.attributes().get("src").contains(".wmf")) {
                    img.removeAttr("width");
                    img.removeAttr("height");
                    img.removeAttr("style");
                    img.attributes().put("width", "800pt");
                }

            }

            Elements spanElements = htmlDoc.body().select("span");
            for(int i=0; i< spanElements.size(); i++){

                Element span = spanElements.get(i);
                String id = span.attributes().get("id");
                if(id != null && id.startsWith("_Toc")){
                    span.remove();
                }

            }


            Elements childrenElements = htmlDoc.body().children();

            if(titleBean == null){
                titleBean = new TitleBean();
            }

            if(titleBean.getTitleList() == null){
                titleBean.setTitleList(Lists.newArrayList());
            }

            Map<String,List<TitleLevel>> titleMap = Maps.newHashMap();
            for(TitleLevel titleLevel : titleBean.getTitleList()){
                if(titleLevel.getText().isEmpty()){
                    continue;
                }

                if(titleMap.containsKey(titleLevel.getText())){
                    List<TitleLevel> tList = titleMap.get(titleLevel.getText());
                    tList.add(titleLevel);
                }else{
                    List<TitleLevel> tList = Lists.newArrayList();
                    tList.add(titleLevel);

                    titleMap.put(titleLevel.getText(),tList);
                }
            }

            List<WordContent> rsList = Lists.newArrayList();

            Integer index = 0;
            for(int i=0; i< childrenElements.size(); i++){
                Element element = childrenElements.get(i);

                if(element.tagName().equals("div")){

                    Elements pElements = element.children();
                    for(int j = 0; j < pElements.size(); j++){
                        rsList.add(parserPElement(index,pElements.get(j),titleMap,titleBean.getStructure()));
                        index++;
                    }

                }else{
                    rsList.add(parserPElement(index,element,titleMap,titleBean.getStructure()));
                    index++;   
                }
            }

            return rsList;
        }catch (Exception e){
            e.printStackTrace();
            throw BizException.of(KgDocumentErrorCodes.DOCUMENT_PARSE_ERROR);
        }

    }

    private static WordContent parserPElement(Integer index, Element element, Map<String,List<TitleLevel>> titleMap, Integer structure){

        String text = element.text();


        List<String> texts = Lists.newArrayList();
        texts.add(text);


        element = parseTagText2Span(element);
       /* List<Element> spans = element.select("span");
        if(spans != null && !spans.isEmpty()){
            texts.addAll(spans.stream().map(span -> span.text()).collect(Collectors.toList()));
        }*/

       if(text.contains(" ") && text.split(" ").length == 2 && Pattern.matches(match,text)){
           texts.add(text.split(" ")[1]);
       }

        String titleText = null;
        for(String t : texts){
            if(titleMap.containsKey(t)){
                titleText = t;
                break;
            }
        }

        if(titleText != null){
            TitleLevel titleLevel = titleMap.get(titleText).get(0);
            if(titleMap.get(titleText).size() > 1){
                titleMap.get(titleText).remove(0);
            }

            WordContent wordContent = WordContent.builder()
                    .htmlText(element.outerHtml())
                    .text(titleText)
                    .level(titleLevel.getLevel())
                    .structure(structure)
                    .index(index)
                    .build();

            return wordContent;

        }else{
            WordContent wordContent = WordContent.builder()
                    .htmlText(element.outerHtml())
                    .level(0)
                    .index(index)
                    .structure(structure)
                    .text(element.text())
                    .build();

            return wordContent;
        }
    }

    public static List<WordContent> setTitleAtt(String targetFileName) {
        return setTitleAtt(targetFileName,null);
    }

    public static Element parseTagText2Span(Element element) {

        List<Node> rsList = Lists.newArrayList();
        List<Node> list = element.childNodes();
        for(int j=0; j<list.size(); j++){
            Node n = list.get(j);
            List<Node> nodes = parseSpan(n);
            if(Objects.nonNull(nodes) && !nodes.isEmpty()){
                rsList.addAll(nodes);
            }
        }

        element.empty();
        for(Node n : rsList){
            element.appendChild(n);
        }

        return element;
    }

    public static List<Node> parseSpan(Node n){

        List<Node> rsList = Lists.newArrayList();
        if(n.nodeName().equals("#text")){

            String outerHtml = n.outerHtml();
            char[] chars = outerHtml.toCharArray();
            for(int i=0; i<chars.length; i++){
                String str = chars[i]+"";
                Element node = new Element("span");
                node.appendText(str);
                rsList.add(node);
            }

        }else if(n.nodeName().equals("span")){
            List<Node> nodes = n.childNodes();

            for(Node node : nodes){
                List<Node> list = parseSpan(node);
                if(Objects.nonNull(list) && !list.isEmpty()){
                    rsList.addAll(list);
                }
            }
        }else{

            Element nodeParent = new Element(n.nodeName());

            if(n.attributes() != null && n.attributes().size() > 0){
                n.attributes().forEach(att -> nodeParent.attr(att.getKey(),att.getValue()));
            }

            List<Node> nodes = n.childNodes();
            for(Node node : nodes){
                List<Node> list = parseSpan(node);
                if(Objects.nonNull(list) && !list.isEmpty()){
                    for(Node nchild : list){
                        nodeParent.appendChild(nchild);
                    }
                }
            }

            rsList.add(nodeParent);
        }

        return rsList;
    }
}
