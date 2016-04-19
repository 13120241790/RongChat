package com.rongchat.response;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class LoginResponse {


    /**
     * code : 200
     * result : {"id":"7w0UxC8IB","token":"VQsXY+tR/c6xQa7PwUSWe3m+Zqx345T3JH/hmDnQ/13sM863Xhp+PSZ+GoFvf7LKOdkcTonQAh8HmJsT5YjDDtM3dpZUIJkw"}
     */

    private int code;
    /**
     * id : 7w0UxC8IB
     * token : VQsXY+tR/c6xQa7PwUSWe3m+Zqx345T3JH/hmDnQ/13sM863Xhp+PSZ+GoFvf7LKOdkcTonQAh8HmJsT5YjDDtM3dpZUIJkw
     */

    private ResultEntity result;

    public void setCode(int code) {
        this.code = code;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public ResultEntity getResult() {
        return result;
    }

    public static class ResultEntity {
        private String id;
        private String token;

        public void setId(String id) {
            this.id = id;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getId() {
            return id;
        }

        public String getToken() {
            return token;
        }
    }
}
