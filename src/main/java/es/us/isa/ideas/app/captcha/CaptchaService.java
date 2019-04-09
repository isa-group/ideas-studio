package es.us.isa.ideas.app.captcha;

public interface CaptchaService {
  void processResponse(final String response) throws ReCaptchaInvalidException;
}