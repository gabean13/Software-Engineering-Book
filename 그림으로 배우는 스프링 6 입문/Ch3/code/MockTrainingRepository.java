package Ch3.code;

import java.util.List;

public class MockTrainingRepository implements TrainingRepository {
    @Override
    public List<Training> selectAll() {
        // 테스트용으로 적당한 데이터를 준비한다.
        return null;
    }
}
