package models.api;

/**
 * The ApiError will be shown if something occur what should not happen.
 * 
 * @author Sebastian Sachtleben
 */
public class ApiError {

	private Integer code;
	private String message;
	private String description;
	private Object body;

	/**
	 * Default constructor with code, message, description and body parameters.
	 * 
	 * @param code
	 *          The code to set
	 * @param message
	 *          The message to set
	 * @param description
	 *          The description to set
	 * @param body
	 *          The body to set
	 */
	public ApiError(Integer code, String message, String description, Object body) {
		this.code = code;
		this.message = message;
		this.description = description;
		this.body = body;
	}

	/**
	 * Default constructor with code, message and description parameters.
	 * 
	 * @param code
	 *          The code to set
	 * @param message
	 *          The message to set
	 * @param description
	 *          The description to set
	 */
	public ApiError(Integer code, String message, String description) {
		this(code, message, description, null);
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
}
