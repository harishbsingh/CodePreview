import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;

import org.junit.Test;

import com.lendup.controllers.TwilioApplication;
import com.lendup.twiliocaller.models.FizzBuzz;

import play.mvc.Result;
import play.test.FakeRequest;
import play.test.Helpers;

/**
 * 
 * Simple (JUnit) tests that can call all parts of a play app. If you are
 * interested in mocking a whole application, see the wiki for more details.
 * 
 */
public class ApplicationTest {

	@Test
	public void testIndex() {
		Result result = Helpers.callAction(controllers.routes.ref.Application
				.index(), new FakeRequest(GET, "/"));
		assertThat(result).isNotNull();
	}

	@Test
	public void testFizzBuzz() {

		// Create and train mock repository
		FizzBuzz fb = new FizzBuzz("10");

		String result = fb.findFIzzBizz();
		assertThat(result).isEqualTo(" 1 2 Fizz 4 Buzz Fizz 7 8 Fizz Buzz");
	}
	
	@Test
	public void test() {

		// Create and train mock repository
		FizzBuzz fb = new FizzBuzz("#10");

		String result = fb.findFIzzBizz();
		assertThat(result).isEqualTo("Invalid Number Input");
	}

}
