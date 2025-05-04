package ducksim

interface QuackBehavior {
    val state: State
    val quackText: String
}

class QuackNormal : QuackBehavior {
    override val state = State.QUACKING
    override val quackText = "Quack!"
}

class QuackNoWay : QuackBehavior {
    override val state = State.SWIMMING
    override val quackText = ""
}

class QuackSqueak : QuackBehavior {
    override val state = State.QUACKING
    override val quackText = "Squeak!"
}

class QuackHonk : QuackBehavior {
    override val state = State.QUACKING
    override val quackText = "Honk"
}

class QuackNoise : QuackBehavior {
    override val state = State.QUACKING
    override val quackText = "Noise!"
}
