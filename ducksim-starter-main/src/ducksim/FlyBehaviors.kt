package ducksim

interface FlyBehavior {
    val state: State
}

class FlyNoWay : FlyBehavior {
    override val state = State.SWIMMING
}

class FlyWithWings : FlyBehavior {
    override val state = State.FLYING
}
