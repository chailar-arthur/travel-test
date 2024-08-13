package com.example.travel.service;

import com.example.travel.entity.Spot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface PageService {
     abstract void generatePdf(OutputStream outputStream, Map<String,Object> map) throws IOException;
}
