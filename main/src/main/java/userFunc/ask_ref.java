package userFunc;

import Neulearn.Func.Func;

public class ask_ref extends Func{

	int cnt = 0;
	
	@Override
	public int run(String... args) {
		
		System.out.println(args[0] + "에 대한 내용은 답변해 드릴 수 없습니다.");
		
		return super.run(args);
		
	}
	
}
