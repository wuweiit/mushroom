package markdown;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = new Markdown4jProcessor().process("This is a **bold** text");
		System.out.println(html);
	}
}
