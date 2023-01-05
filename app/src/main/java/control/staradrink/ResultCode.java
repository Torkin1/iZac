package control.staradrink;

public enum ResultCode {
    ERROR (-1),
    OK (0),
    NOT_STARRED(1),
    STARRED(2);

    private final int code;

    private ResultCode (int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
}
