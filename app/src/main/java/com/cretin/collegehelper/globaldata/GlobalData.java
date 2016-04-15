package com.cretin.collegehelper.globaldata;

import com.cretin.collegehelper.model.Images;
import com.cretin.collegehelper.ui.SendActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cretin on 16/3/6.
 */
public class GlobalData {
    private static GlobalData globalData;

    /**
     * 选择和编辑的全局数据
     */
    private List<Images> imagesList = new ArrayList<>();


    /**
     * 待发的图片资源列表
     */
    private List<Images> toSendImagesList = new ArrayList<>();


    /**
     * 保存的SendFlowActivity, 可以获取当前已有多少个资源
     */
    private SendActivity sendFlowActivity;

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // 相册列表, 不用intent传
    public List<String> getListAbulm() {
        List<String> newTemp = new ArrayList<>();
        newTemp.addAll(listAlbum);
        return listAlbum;
    }

    public void setListAbulm(List<String> listAlbum) {
        List<String> newTemp = listAlbum;
        this.listAlbum = newTemp;
    }

    private List<String> listAlbum = new ArrayList<>();

    public SendActivity getSendFlowActivity() {
        return sendFlowActivity;
    }

    public void setSendFlowActivity(SendActivity sendFlowActivity) {
        this.sendFlowActivity = sendFlowActivity;
    }

    /**
     * 发布所用的数据
     * @return
     */
    public List<Images> getToSendImagesList() {
        List<Images> newToSendImagesList = new ArrayList<>();
        newToSendImagesList.addAll(toSendImagesList);
        return newToSendImagesList;
    }


    public void setToSendImagesList(List<Images> toSendImagesList) {
        List<Images> tempList = new ArrayList<>();
        tempList.addAll(toSendImagesList);
        this.toSendImagesList = tempList;
    }



    private GlobalData(){}

    public List<Images> getImagesList() {
        List<Images> newImageList = new ArrayList<>();
        newImageList.addAll(imagesList);
        return newImageList;
    }

    public void setImagesList(List<Images> imagesList) {
        List<Images> tempList = new ArrayList<>();
        tempList.addAll(imagesList);
        this.imagesList = tempList;
    }

    public synchronized static GlobalData getInstance(){
        if(globalData == null){
            globalData = new GlobalData();
        }

        return globalData;
    }

}
