package ai.plantdata.kgcloud.plantdata.rsp.app;


import ai.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import lombok.Data;

@Data
public class EntityBeanAnalysisa extends EntityBean {

	private Double x;
	private Double y;
	private Long cluster;
	private Double distance;

}
