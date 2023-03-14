package pack;
import java.util.Random;

public class test {
int id=10;
private String content = null;

    void download() {
		Random random = new Random();
		int numWords = 1 + random.nextInt(10);
		String content = "";
		for (int i = 0; i < numWords; i++)
			content += "word" + (random.nextInt(5) + 1) + " ";
		this.content = content;
		// Fake wait for web-response.
		try {
			Thread.sleep(200);
		}
		catch (InterruptedException exception) {
			System.out.println(exception);
		}
		System.out.println("Downloaded: " + id);
	}
}
