package com.plantdata.kgcloud.domain.dataset.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataOptConnect {

    private String addresses;
    private String username;
    private String password;
    private String database;
    private String table;
}
