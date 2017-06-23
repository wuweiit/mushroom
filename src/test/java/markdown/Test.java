package markdown;

import io.github.gitbucket.markedj.Marked;

import java.io.IOException;


public class Test {

	public static void main(String[] args) throws IOException {
		String html = Marked.marked("This is a **bold** text");
		System.out.println(html);
	}
}
