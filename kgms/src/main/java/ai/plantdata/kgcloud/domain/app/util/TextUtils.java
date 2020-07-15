package ai.plantdata.kgcloud.domain.app.util;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/14 11:08
 */
public class TextUtils {

    public static void exportJson(String exportFileName, String exportContent, HttpServletResponse response) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {

            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] b = exportContent.getBytes();
            byteArrayOutputStream.write(b, 0, b.length - 1);
            is = new ByteArrayInputStream(b);
            // 设置response参数
            response.reset();
            response.setContentType("multipart/form-data;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((exportFileName + ".txt").getBytes(), StandardCharsets.ISO_8859_1));
            ServletOutputStream out = response.getOutputStream();

            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[4096];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length - 1))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }

                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (is != null) {
                    is.close();
                }

                if (bos != null) {
                    bos.close();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
