package preProc;
import java.util.List;

import Neulearn.Func.constants.GlobalConst;
import Neulearn.util.preProccer.PreProcController;
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class KR_PreProc extends PreProcController{

	Komoran komoran = null;
	
	public void init(){
		
		//RootPath 에 코모란 형태소 분석기 모델을 위치시켜야한다.
		komoran = new Komoran(GlobalConst.ROOT_PATH + "models");
		
	}
	
	// getPreProcString 는 처리 완료된 문자열을 반환한다.
	// 따라서 이 메소드를 오버라이드하여 커스터마이징한다.
	@Override
	public String getPreProcString(String raws) {
		
		String output = "";
		
		if (komoran == null)init();
	
		List<List<Pair<String,String>>> result = komoran.analyze(raws);
		for (List<Pair<String, String>> eojeolResult : result) {
		for (Pair<String, String> wordMorph : eojeolResult) {
			 
			output += wordMorph.getFirst() + " ";
			if (wordMorph.getFirst().equals("."))output += "\n";
		}
		}
		
		output = output.replaceAll("\\d+", "d");
		
		return output.replaceAll("\n+", "\n");
	}
	
	 
	
}
