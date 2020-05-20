package cn.sessiontech.xcx.dto.weixin;

import lombok.Data;

/**
 * @author Administrator
 * @classname ttt
 * @description TODO
 * @date 2019/10/21 22:39
 */
@Data
public class ttt {

    /**
     * touser : OPENID
     * template_id : TEMPLATE_ID
     * page : index
     * data : {"number01":{"value":"339208499"},"date01":{"value":"2015年01月05日"},"site01":{"value":"粤海喜来登酒店"},"site02":{"value":"广州市天河区天河路208号"}}
     */

    private String touser;
    private String template_id;
    private String page;
    private DataBean data;
    @Data
    public static class DataBean {
        /**
         * number01 : {"value":"339208499"}
         * date01 : {"value":"2015年01月05日"}
         * site01 : {"value":"粤海喜来登酒店"}
         * site02 : {"value":"广州市天河区天河路208号"}
         */

        private Number01Bean number01;
        private Date01Bean date01;
        private Site01Bean site01;
        private Site02Bean site02;
        @Data
        public static class Number01Bean {
            /**
             * value : 339208499
             */

            private String value;

        }
        @Data
        public static class Date01Bean {
            /**
             * value : 2015年01月05日
             */

            private String value;

        }
        @Data
        public static class Site01Bean {
            /**
             * value : 粤海喜来登酒店
             */

            private String value;
        }
        @Data
        public static class Site02Bean {
            /**
             * value : 广州市天河区天河路208号
             */

            private String value;

        }
    }
}
