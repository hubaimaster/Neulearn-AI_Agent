# Neulearn-AI_Agent
대화형 에이전트 프로젝트 - Neulearn


*해당 프로젝트는 Neulearn.jar 라이브러리(Core)를 포함하고 있고,
Word2Vec - DeepLearning4Java 의존성을 pom.xml 파일에 포함하고 있으며,
전처리기에 쓰이는 형태소 분석 라이브러리 - Komoran 라이브러리를 포함하고 있습니다.
 
 

- 아래는 Neulearn 대화형 에이전트 프로젝트의 프로세스를 간단히 나타낸 것입니다.

1. [ROOT]/corpus 폴더의 .txt 파일들을 Word2Vec로 학습하여 [ROOT]/vectors.txt 파일로 저장합니다.(충분히 많은 양의 텍스트가 필요합니다)
2. 학습 말뭉치의 문장에 대하여 Word2Vec으로 문장의 벡터를 구합니다.
3. 얻은 벡터를 ANN (다층 피드포워드 신경망 - MLP)의 파라미터로 제공하여 말뭉치에 작성된 함수와 매칭시켜 학습합니다.
4. 학습이 완료된 ANN을 이용하여 새로 들어온 문장에 대해 매칭되는 클래스 이름을 출력합니다.
5. 해당 클래스(Neulearn.Func.Func 클래스 상속)를 JAVA Reflection 을 이용하여 찾아냅니다.
6. 해당 클래스의 public int run(String... args)메소드를 실행합니다.



Neulearn.jar 라이브러리의 기본적인 사용법을 Main.java 클래스에 포함하고 있습니다.

Neulearn.jar 라이브러리는 문장을 입력받아 특정 동작과 매칭시켜주는 작업을 진행합니다.

매칭 기준은 Root 폴더의 아래 있는 senToFuncCorpus.txt(말뭉치) 를 학습하여 얻을 수 있습니다.

특정 동작들은 개발자가 Neulearn.Func.Func 를 커스텀 클래스에 상속(extend)하여 구현할 수 있습니다.
( run(String... args) 메소드를 @Override 하여 원하는 작업을 작성해주시면 됩니다 )

해당 동작들을 적용하려면, 

Reflector.userControllerPath = "Neulearn.Func.userFunc";

와 같이 동작을 정의해둔 클래스가 있는 패키지의 이름을 선언 해줘야합니다.

전처리 클래스 또한 커스터마이징이 가능합니다.

전처리 클래스의 경우,

GlobalConst.preProcController = new KR_PreProc();

와 같이 선언하사면 되고 전처리기를 커스터마이징하여 사용하시고 싶은 경우,

Neulearn.util.preProccer.PreProcController 클래스를 상속하여 커스텀 클래스를 작성해주시면 됩니다.
(public String getPreProcString(String raws) 메소드를 @Override 해주셔서 전처리 프로세스를 작성하시면 됩니다)


(* GlobalConst.ROOT_PATH의 값을 /datas 폴더의 경로로 설정하셔야 합니다. )


- 예시 코드:


public class Main 
{

public static void main( String[] args ) throws Exception
{

//루트 경로 정의 
GlobalConst.ROOT_PATH = "./datas/";

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

//"/tmps/vectors.txt" 에 위치한 벡터를 불러온다.
Word2VecRaw.loadVectorWord();

//문장을 사용자 정의 클래스와 매칭시켜주는 함수를 만드는 Trainer
SentenceToFuncTrainer funcTrainer = new SentenceToFuncTrainer();

//가중치 배열이 정해진 신경망을 직렬화하여 저장 "/tmps/ann.bin", "/tmps/funclist.bin"
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