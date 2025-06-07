package com.zogirdex.pcmonitorserver;

import java.util.List;

/**
 *
 * @author tom3k
 */
public class SensorUploadRequest {
    public String computerId;
    public List<SensorReadingDTO> readings;
}
