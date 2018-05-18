package com.dream.NiuFaNet.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3 0003.
 */
public class ChatBean implements Serializable{


    /**
     * body : {"answer":"中华人民共和国境内的企业、个体经济组织、民办非企业单位等组织与劳动者建立劳动关系，订立、履行、变更、解除或者终止劳动合同，以及国家机关、事业单位、社会团体和与其建立劳动关系的劳动者，订立、履行、变更、解除或者终止劳动合同，均适用《劳动合同法》。","url":"","related":[{"question":"公司可以以分公司或办事处的名义与劳动者签订劳动合同吗？","outline":"劳动合同主体\u2014\u2014单位","logogram":"分公司/办事处怎么签"},{"question":"公务员适用劳动合同法吗？","outline":"劳动合同主体-单位","logogram":"公务员签劳动合同吗"},{"question":"国家机关（或事业单位）临时聘用的人员，需要签订劳动合同吗？","outline":"劳动合同主体-单位","logogram":"临时聘用人员怎么签"},{"question":"尚未毕业的在校学生与单位建立的劳动关系合法吗？","outline":"劳动合同的履行和变更","logogram":"可以录用在校生吗"},{"question":"劳动合同怎么签？","outline":"劳动合同签订","logogram":"劳动合同怎么签"},{"question":"员工拒绝签订劳动合同怎么办？","outline":"劳动合同签订","logogram":"员工拒绝签订怎么办"}]}
     * error : false
     */

    private BodyBean body;
    private String error;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class BodyBean implements Serializable{
        /**
         * answer : 中华人民共和国境内的企业、个体经济组织、民办非企业单位等组织与劳动者建立劳动关系，订立、履行、变更、解除或者终止劳动合同，以及国家机关、事业单位、社会团体和与其建立劳动关系的劳动者，订立、履行、变更、解除或者终止劳动合同，均适用《劳动合同法》。
         * url :
         * related : [{"question":"公司可以以分公司或办事处的名义与劳动者签订劳动合同吗？","outline":"劳动合同主体\u2014\u2014单位","logogram":"分公司/办事处怎么签"},{"question":"公务员适用劳动合同法吗？","outline":"劳动合同主体-单位","logogram":"公务员签劳动合同吗"},{"question":"国家机关（或事业单位）临时聘用的人员，需要签订劳动合同吗？","outline":"劳动合同主体-单位","logogram":"临时聘用人员怎么签"},{"question":"尚未毕业的在校学生与单位建立的劳动关系合法吗？","outline":"劳动合同的履行和变更","logogram":"可以录用在校生吗"},{"question":"劳动合同怎么签？","outline":"劳动合同签订","logogram":"劳动合同怎么签"},{"question":"员工拒绝签订劳动合同怎么办？","outline":"劳动合同签订","logogram":"员工拒绝签订怎么办"}]
         */

        private String answer;
        private String type;
        private String url;
        private List<RelatedBean> related;
        private String fdType;
        private List<InputGetBean> scheduleData;

        public List<InputGetBean> getScheduleData() {
            return scheduleData;
        }

        public void setScheduleData(List<InputGetBean> scheduleData) {
            this.scheduleData = scheduleData;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFdType() {
            return fdType;
        }

        public void setFdType(String fdType) {
            this.fdType = fdType;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<RelatedBean> getRelated() {
            return related;
        }

        public void setRelated(List<RelatedBean> related) {
            this.related = related;
        }

        public static class RelatedBean implements Serializable{
            /**
             * question : 公司可以以分公司或办事处的名义与劳动者签订劳动合同吗？
             * outline : 劳动合同主体——单位
             * logogram : 分公司/办事处怎么签
             */

            private String question;
            private String outline;
            private String logogram;

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getOutline() {
                return outline;
            }

            public void setOutline(String outline) {
                this.outline = outline;
            }

            public String getLogogram() {
                return logogram;
            }

            public void setLogogram(String logogram) {
                this.logogram = logogram;
            }
        }
    }


}
