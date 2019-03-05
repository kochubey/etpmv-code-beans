package ru.efive.esb.webconfig.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DatasourceUtils {

    private final ResourceUtils resourceUtils;

    @Autowired
    public DatasourceUtils(ResourceUtils resourceUtils) {
        this.resourceUtils = resourceUtils;
    }

    public List<String> getPtsList() {
        return resourceUtils.getList("");
    }

    public List<String> getDtsList(String ptsId) {
        return resourceUtils.getList(ptsId, "");
    }

    public List<String> getAllDtsList() {
        List<String> ptsList = getPtsList();
        List<String> dtsList = new ArrayList<>();
        for (String ptsId : ptsList) {
            dtsList.addAll(getDtsList(ptsId));
        }
        return dtsList;
    }

    public List<String> getDtsVersions(String ptsId, String dtsId) {
        return resourceUtils.getList(ptsId, dtsId);
    }

    public List<String> getSubscribers(String ptsId, String dtsId, String dtsVersion) {
        return resourceUtils.getList(ptsId, dtsId, dtsVersion, "");
    }
}