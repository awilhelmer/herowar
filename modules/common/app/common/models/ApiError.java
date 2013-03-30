package common.models;

/**
 * The ApiError will be shown if something occur what should not happen.
 * 
 * @author Sebastian Sachtleben
 */
public class ApiError {

  private Integer code;
  private String message;

  /**
   * Default constructor with code and message parameters.
   * 
   * @param code
   *          The code to set
   * @param message
   *          The message to set
   */
  public ApiError(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
