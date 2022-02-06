package cn.dustlight.storaging.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Configuration {

    private String clientId,userId,name;
    private Map<String,?> data;

}
