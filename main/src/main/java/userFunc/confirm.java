package userFunc;

import Neulearn.Func.Func;

public class confirm extends Func{

	@Override
	public int run(String... args) {
		 
		System.out.println("네 맞습니다.");
		
		return super.run(args);
	}
	
}

