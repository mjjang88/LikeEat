## GIT 관리 규칙

* 개발용 브랜치는 develop 입니다
  * master는 사용하지 말아주세요
* 개발용 브랜치인 develop에 직접 붙어서 작업하지 말아주세요
  * develop을 부모로 하는 feature 브랜치를 생성하여 작업하고 develop 브랜치에 merge 해 주세요
  * ex) 브랜치명 예시 : feature/add_pin_point
* develop 브랜치에 빌드가 실패하도록 두지 마세요
  * merge 후 빌드해보고 실패 시 바로 수정해 주세요
