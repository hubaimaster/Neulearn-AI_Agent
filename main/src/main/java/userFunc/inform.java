package userFunc;

import Neulearn.Func.Func;

public class inform extends Func{

	@Override
	public int run(String... args) {
		 
		System.out.println(args[0] + "라고요? 알겠습니다.");
		
		return super.run(args);
	}
	
}
