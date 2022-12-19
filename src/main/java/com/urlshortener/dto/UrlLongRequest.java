package com.urlshortener.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UrlLongRequest {

    @ApiModelProperty(required = true, notes = "Url to convert to short")
    private String longUrl;

}