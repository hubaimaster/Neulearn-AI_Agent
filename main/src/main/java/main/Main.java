package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Neulearn.Func.Reflector;
import Neulearn.Func.constants.GlobalConst;
import Neulearn.Func.util.CorpusMerger;
import Neulearn.sentenceToFunc.action.SentenceToFuncActor;
import Neulearn.sentenceToFunc.train.SentenceToFuncTrainer;
import Neulearn.vectorizer.Word2VecRaw;
import preProc.KR_PreProc;
 

public class Main 
{
 
    public static void main( String[] args ) throws Exception
    {
    	
    	//루트 경로 정의 
    	GlobalConst.ROOT_PATH = "/Users/kimchanghwan/Desktop/datas/";
    	
    	//말뭉치, 입력문장 전처리에 사용될 전처리기 (PreProcController 클래스를 상속하여 커스텀 가능)
    	GlobalConst.preProcController = new KR_PreProc();
    	
    	//사용자 정의 행동(클래스)가 위치할 패키지 경로 정의
    	Reflector.userControllerPath = "Neulearn.Func.userFunc";
      
    	//상수 정의
    	GlobalConst.FUNC_COUNT = 9; //정의 행동(클래스) 개수
    	GlobalConst.FUNC_N_HIDDEN = 100; //은닉 뉴런 개수
    	GlobalConst.FUNC_EPOCH = 7000; //최대 반복 학습 횟수
    	GlobalConst.FUNC_LIMIT_ERR = 0.001; //최소 에러 수치
    	GlobalConst.FUNC_LEARN_RATE = 0.002; //학습률 
    	GlobalConst.WORC2VEC_LAYER_SIZE = 200; //워드 투 벡터 레이어 원소개수
    	
    	
    	
    	
    	//GlobalConst.ROOT_PATH + "/tmps/corpus" 경로에 있는 모든 txt파일들을 
    	//하나로 묶은 뒤 전처리를 시행
    	CorpusMerger.mergeCorpus();
    	
    	//Word2Vec으로 단어들을 벡터화 시켜 "/tmps/vectors.txt"에 위치시킨다.
    	Word2VecRaw.makeVocaFile();
    	
    	
    	
    	//"/vectors.txt" 에 위치한 벡터를 불러온다.
    	Word2VecRaw.loadVectorWord();
    	
    	//문장을 사용자 정의 클래스와 매칭시켜주는 함수를 만드는 Trainer
    	SentenceToFuncTrainer funcTrainer = new SentenceToFuncTrainer();
     
    	//가중치 배열이 정해진 신경망을 직렬화하여 저장 "/tmps/ann.bin", "/tmps/funclist.bin"
    	//이 코드는 한번 실행하면 save파일이 작성되기 때문에 새롭게 학습하고자 할 때만 실행해주면 된다.
    	funcTrainer.makeBinFiles();
      
    	//트레이너가 훈련시킨 신경망으로 문장을 입력받아 softmax를 취해 함수를 매칭시켜준다.
    	SentenceToFuncActor funcActor = new SentenceToFuncActor();
    	
    	//분석 프로세스 테스트.
    	startProc(funcActor);
    	 
    }
    
    public static void startProc(SentenceToFuncActor actor) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
    	
    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    
    	Reflector reflector = new Reflector();
    	
    	System.out.println("문장을 입력하시오:");
    	
    	while(true){
    		
    		String s = bufferedReader.readLine();
    		
    		//입력된 문장을 기반으로 매칭될 클래스의 이름을 구한다.
    		String className = actor.getFuncClassName(s);
    		
    		System.out.println(s + " -> " + className);
    		
    		//사용자 정의 패키지에 있는 클래스를 실행.
    		//입력된 본래 문장을 해당 클래스 내의 run함수의 파라미터로 넘긴다 (args[0])
    		reflector.runClass(className, s);
    		
    	}
    	
    }
    
}