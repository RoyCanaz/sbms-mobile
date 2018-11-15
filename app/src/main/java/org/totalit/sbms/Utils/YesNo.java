package org.totalit.sbms.Utils;

public enum YesNo {
    YES(1), NO(2);

    private final Integer code;

    private String name;

    YesNo(Integer code) {
        this.code = code;
    }

    public static YesNo get(Integer code) {
        for (YesNo item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Uknown parameter passed to method:");
    }

    public Integer getCode() {
        return code;
    }

    public String getName(){
        return StringUtilis.toCamelCase3(super.name());
    }

    public void setName(String name) {
        this.name = name;
    }
}
