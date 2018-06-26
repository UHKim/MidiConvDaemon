# Midi Convert Daemon
## Why?

* Kotlin을 한번 써보자
* Embed 작업한 쪽에서 Socket으로만 보낼 수 있다는데 이쪽은 DMX/OSC/MIDI 규격임
* 중간에 Gateway가 필요!

## Feature
* Socket으로 들어온 ASCII를 Mapping 하여 loopback MIDI Device에 Signal을 .

## Requirements

* Win PC
* TeVirtualMidi (Tobias Erichsen's virtualMIDI SDK)
    * http://www.tobias-erichsen.de/software/virtualmidi/virtualmidi-sdk.html

## How?

* Socket을 받자
* Mapping을 해주자
* 우리쪽 규격으로 바꿔서 보내주자

## Future Issue?

* Kotlin은 쓸만하다.
* Midi Library들은 Platform Dependancy(Win/Mac)가 강하다.
* Processing도 밑단은 자바인데 이걸 Kotlin-based로 바꿀 수 없을까?
