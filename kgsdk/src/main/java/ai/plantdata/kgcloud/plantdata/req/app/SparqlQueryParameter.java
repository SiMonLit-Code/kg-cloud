package ai.plantdata.kgcloud.plantdata.req.app;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SparqlQueryParameter {

    private String kgName;

    private String query;

    private Integer size;

}
