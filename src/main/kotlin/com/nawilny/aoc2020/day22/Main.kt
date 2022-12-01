package com.nawilny.aoc2020.day22

private enum class Player { P1, P2 }

private interface Deck {
    fun isEmpty(): Boolean
    fun getFirst(): Int
    fun put(c: Int)
    fun toList(): List<Int>
    fun copy(i: Int): Deck
    fun size(): Int
}

private class Deck2(val cards: MutableList<Int>) : Deck {

    override fun isEmpty() = cards.isEmpty()

    override fun getFirst() = cards.removeAt(0)

    override fun put(c: Int) {
        cards.add(c)
    }

    override fun toList() = cards

    override fun copy(i: Int): Deck {
        val l = mutableListOf<Int>()
        l.addAll(cards.subList(0, i))
        return Deck2(l)
    }

    override fun size() = cards.size
}

private fun determineWinner(p1Card: Int, p1Deck: Deck, p2Card: Int, p2Deck: Deck): Player {
    //     part1
//    return if (p1Card > p2Card) Player.P1 else Player.P2

    // part2
    if (p1Card <= p1Deck.size() && p2Card <= p2Deck.size()) {
        return playGame(p1Deck.copy(p1Card), p2Deck.copy(p2Card)).first
    }
    return if (p1Card > p2Card) Player.P1 else Player.P2
}

private fun playGame(p1Deck: Deck, p2Deck: Deck): Pair<Player, Deck> {
    val playedRounds = mutableSetOf<Pair<List<Int>, List<Int>>>()
    while (!(p1Deck.isEmpty() || p2Deck.isEmpty())) {
        val round = Pair(p1Deck.toList(), p2Deck.toList())
        if (playedRounds.contains(round)) {
            return Pair(Player.P1, p1Deck)
        }
        playedRounds.add(round)

        val p1Card = p1Deck.getFirst()
        val p2Card = p2Deck.getFirst()
        when (determineWinner(p1Card, p1Deck, p2Card, p2Deck)) {
            Player.P1 -> {
                p1Deck.put(p1Card)
                p1Deck.put(p2Card)
            }
            Player.P2 -> {
                p2Deck.put(p2Card)
                p2Deck.put(p1Card)
            }
        }
    }

    return if (p1Deck.isEmpty()) Pair(Player.P2, p2Deck) else Pair(Player.P1, p1Deck)
}

fun main() {
//    val player1 = listOf(9, 2, 6, 3, 1)
//    val player2 = listOf(5, 8, 4, 7, 10)
//    val player1 = listOf(43, 19)
//    val player2 = listOf(2, 29, 14)
    val player1 = listOf(41, 33, 20, 32, 7, 45, 2, 12, 14, 29, 49, 37, 6, 11, 39, 46, 47, 38, 23, 22, 28, 10, 36, 35, 24)
    val player2 = listOf(17, 4, 44, 9, 27, 18, 30, 42, 21, 26, 16, 48, 8, 15, 34, 50, 19, 43, 25, 1, 13, 31, 3, 5, 40)

    val gameResult = playGame(Deck2(player1.toMutableList()), Deck2(player2.toMutableList()))
    println("Winner: ${gameResult.first} - ${gameResult.second.toList()}")

    val score = gameResult.second.toList().reversed().withIndex().fold(0) { acc, i -> acc + ((i.index + 1) * i.value) }
    println(score)
    // 30695
}
