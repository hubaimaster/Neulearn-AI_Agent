package userFunc;

import Neulearn.Func.Func;

public class req_act extends Func{

	@Override
	public int run(String... args) {
		 
		System.out.println(args[0] + "을 해드리겠습니다.");
		
		return super.run(args);
	}
	
}
