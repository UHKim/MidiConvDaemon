# Midi Convert Daemon
## Why?

* Kotlin을 한번 써보자
* Embed 작업한 쪽에서 Socket으로만 보낼 수 있다는데 이쪽은 DMX/OSC/MIDI 규격임
* 중간에 Gateway가 필요!

## Feature
* Socket으로 들어온 ASCII로 loopback MIDI Device에 Signal을 생성.

## Requirements

* Win PC
* TeVirtualMidi (Tobias Erichsen's virtualMIDI SDK)
    * http://www.tobias-erichsen.de/software/virtualmidi/virtualmidi-sdk.html

## 결론?

* Midi Library들은 Platform Dependancy(Win/Mac)가 강하다.

## Future Issue?

* Processing도 밑단은 자바인데 이걸 Kotlin-based로 바꿀 수 없을까?
