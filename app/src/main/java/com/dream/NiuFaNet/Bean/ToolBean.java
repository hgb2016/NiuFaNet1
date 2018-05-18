package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by hou on 2018/5/5.
 */

public class ToolBean {
    private  String userId;
    private List<ToolBean.DataBean> tools;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<DataBean> getTool() {
        return tools;
    }

    public void setTool(List<DataBean> tool) {
        this.tools = tool;
    }

    public static class DataBean{
        private String toolId;

        public String getToolId() {
            return toolId;
        }

        public void setToolId(String toolId) {
            this.toolId = toolId;
        }
    }
}
