package ducksim

object DuckFactory : Subject() {
    fun createDuck(baseDuck: Duck, starCount: Int, moonCount: Int, crossCount: Int): Duck {
        var duck = baseDuck

        val star = starCount.coerceAtLeast(0)
        val moon = moonCount.coerceAtLeast(0)
        val cross = crossCount.coerceAtLeast(0)

        val total = star + moon + cross
        val allowedTotal = total.coerceAtMost(3)

        // Apply blings in order: Star -> Moon -> Cross
        var remaining = allowedTotal
        repeat(star.coerceAtMost(remaining)) {
            duck = StarBling(duck)
            remaining--
        }
        repeat(moon.coerceAtMost(remaining)) {
            duck = MoonBling(duck)
            remaining--
        }
        repeat(cross.coerceAtMost(remaining)) {
            duck = CrossBling(duck)
            remaining--
        }

        notifyObserver()
        return duck
    }

}
