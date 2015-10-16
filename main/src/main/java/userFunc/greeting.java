package userFunc;

import Neulearn.Func.Func;

public class greeting extends Func{

	@Override
	public int run(String... args) {
		 
		System.out.println("안녕하세요 반가워요");
		
		return super.run(args);
	}
	
}
