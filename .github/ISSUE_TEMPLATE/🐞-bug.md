---
name: "\U0001F41E Bug"
about: Bug 발생 시 작성해주세요.
title: "<버그 요약> 버그 해결"
labels: "\U0001F41E 버그"
assignees: ''

---

# 목차
- [개요](#개요)
- [발견 사항](#발견-사항)
- [스크린샷/GIF](#스크린샷gif)
- [제안](#제안)
- [메모](#메모)
  - [학습](#학습)
  - [참고 사이트](#참고-사이트)

# 개요
> 버그가 어느 부분에서 발생하는지 적습니다.
> 
> ex. Fragment에 MapView를 적용할 때 발생한 에러

</br>

# 발견 사항
> 구체적으로 버그가 언제, 어떻게, 왜 발생하는지 적습니다.
> 
>  ex. 
> 
> `java.lang.ClassCastException: dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper cannot be cast to android.app.Activity`
> 
> Fragment에서 Context를 가져올 때 FragmentContextWrapper를 사용하는데, 여기서 가져오는 Context가 Activity의 Context가 아니어서 발생하는 문제.
> 
> 이를 Hilt에서는 인식하지 못해 런타임 시에 크래시가 발생한다.

</br>

# 스크린샷/GIF
> 문제 발생을 스크린샷 or Gif로 넣습니다.

</br>

# 제안
> 문제를 해결하기 위한 방안을 제시합니다.
> 
> ex. 기존 MapView를 FrameLayout으로 대체하고 코드 상에서 동적으로 추가한다. 그리고 이때 context를 baseContext로 넣어주자.

</br>

# 메모
## 학습
> 버그를 해결하면서 학습한 내용 or 학습 키워드를 정리합니다.
> 
> 만약 없다면 비워둬도 괜찮습니다!

## 참고 사이트
> 해결하면서 참고한 사이트를 적습니다.
