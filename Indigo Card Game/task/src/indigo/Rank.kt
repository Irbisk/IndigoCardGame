package indigo

var deck = createDeck()
val tableCards = mutableListOf<Card>()
var gameIsOver = false
val playerHuman = Player(false)
val playerComputer = Player(true)
var playerHumanMovedFirst: Boolean? = null
var playerHumanWonLast: Boolean? = null

class Card(val rank: Rank, val suit: Suit) {
    override fun toString(): String {
        return "${rank.symbol}${suit.symbol}"
    }
}

fun createDeck(): MutableList<Card> {
    val list = mutableListOf<Card>()
    for (suit in Suit.values()) {
        for (rank in Rank.values()) {
            list.add(Card(rank, suit))
        }
    }
    return list
}

class Player(private val isComputer: Boolean) {
    val cardsInHandList = mutableListOf<Card>()
    var isPlayerTurn = true
    var cardsList = mutableListOf<Card>()
    var score = 0

    fun getUpTo6Cards() {
        while (this.cardsInHandList.size < 6) {
            if (deck.isEmpty()) {
                break
            }
            this.cardsInHandList.add(deck.removeLast())
        }
    }

    fun chooseCardToPlay() {
        if (!this.isComputer) {
            showCardsInHand()
            while (true) {
                println("Choose a card to play (1-${this.cardsInHandList.size}):")
                val input = readln()
                if (input == "exit") {
                    gameIsOver = true
                    break
                }
                val number = input.toIntOrNull()
                if (number != null && number in 1..this.cardsInHandList.size) {
                    tableCards.add(this.cardsInHandList.removeAt(number - 1))
                    break
                }
            }
        } else {
            showCardsInHandOfComputer()
            val card = getComputerCard()
            var number = 0
            for (i in this.cardsInHandList.indices) {
                if (card.suit == this.cardsInHandList[i].suit && card.rank == this.cardsInHandList[i].rank) {
                    number = i
                }
            }

            tableCards.add(this.cardsInHandList.removeAt(number))
            println("Computer plays ${tableCards[tableCards.lastIndex]}")
        }
        if (!gameIsOver) {
            checkMove()

            if (this.cardsInHandList.isEmpty()) {
                this.getUpTo6Cards()
            }

            swapMove()
            println()
        }
    }

    private fun getComputerCard(): Card {
        if (this.cardsInHandList.size == 0) {
            return this.cardsInHandList[0]
        } else if (tableCards.isEmpty()) {
            val mapSameSuit = this.cardsInHandList.groupBy { it.suit }
            val sameSuitList = mapSameSuit.values.filter { it.size > 1 }.flatten()
            return if (sameSuitList.isNotEmpty()) {
                sameSuitList[0]
            } else {
                val mapSameRank = this.cardsInHandList.groupBy { it.rank }
                val sameRankList = mapSameRank.values.filter { it.size > 1 }.flatten()
                if (sameRankList.isNotEmpty()) {
                    sameRankList[0]
                } else {
                    this.cardsInHandList[0]
                }
            }
        } else {
            val candidatesList = mutableListOf<Card>()
            cardsInHandList.filter { it.suit == tableCards[tableCards.lastIndex].suit || it.rank == tableCards[tableCards.lastIndex].rank }
                .forEach { candidatesList.add(it) }

            if (candidatesList.isEmpty()) {
                val mapSameSuit = this.cardsInHandList.groupBy { it.suit }
                val sameSuitList = mapSameSuit.values.filter { it.size > 1 }.flatten()
                return if (sameSuitList.isNotEmpty()) {
                    sameSuitList[0]
                } else {
                    val mapSameRank = this.cardsInHandList.groupBy { it.rank }
                    val sameRankList = mapSameRank.values.filter { it.size > 1 }.flatten()
                    if (sameRankList.isNotEmpty()) {
                        sameRankList[0]
                    } else {
                        this.cardsInHandList[0]
                    }
                }
            } else if (candidatesList.size == 1) {
                return candidatesList[0]
            } else {
                val mapSameSuit = candidatesList.groupBy { it.suit }
                val sameSuitList = mapSameSuit.values.filter { it.size > 1 }.flatten()
                return if (sameSuitList.isNotEmpty()) {
                    sameSuitList[0]
                } else {
                    val mapSameRank = candidatesList.groupBy { it.rank }
                    val sameRankList = mapSameRank.values.filter { it.size > 1 }.flatten()
                    if (sameRankList.isNotEmpty()) {
                        sameRankList[0]
                    } else {
                        candidatesList[0]
                    }
                }
            }
        }
    }

    private fun checkMove() {
        if (tableCards.size > 1) {
            val cardTop1 = tableCards[tableCards.lastIndex]
            val cardTop2 = tableCards[tableCards.lastIndex - 1]
            if (cardTop1.rank == cardTop2.rank || cardTop1.suit == cardTop2.suit) {
                while (tableCards.isNotEmpty()) {
                    this.cardsList.add(tableCards.removeLast())
                    this.score += this.cardsList[cardsList.lastIndex].rank.score
                }
                playerHumanWonLast = if (this.isComputer) {
                    println("Computer wins cards")
                    false
                } else {
                    println("Player wins cards")
                    true
                }
                showScore()
            }
        }
    }

    private fun showCardsInHand() {
        var count = 1
        print("Cards in hand: ")
        this.cardsInHandList.forEach { print("${count++})$it ") }
        println()
    }

    private fun showCardsInHandOfComputer() {
        this.cardsInHandList.forEach { print("$it ") }
        println()
    }
}

fun addFinalScore() {
    if (playerHuman.cardsList.size == playerComputer.cardsList.size) {
        if (playerHumanMovedFirst == true) {
            playerHuman.score += 3
        } else {
            playerComputer.score += 3
        }
    } else if (playerHuman.cardsList.size > playerComputer.cardsList.size) {
        playerHuman.score += 3
    } else {
        playerComputer.score += 3
    }
}

fun showScore() {
    println("Score: Player ${playerHuman.score} - Computer ${playerComputer.score}")
    println("Cards: Player ${playerHuman.cardsList.size} - Computer ${playerComputer.cardsList.size}")
}

private fun swapMove() {
    playerHuman.isPlayerTurn = !playerHuman.isPlayerTurn
    playerComputer.isPlayerTurn = !playerComputer.isPlayerTurn
}

enum class Rank (val symbol: String, val score: Int) {
    ACE("A", 1),
    TWO("2", 0),
    THREE("3", 0),
    FOUR("4", 0),
    FIVE("5", 0),
    SIX("6", 0),
    SEVEN("7", 0),
    EIGHT("8", 0),
    NINE("9", 0),
    TEN("10", 1),
    JACK("J", 1),
    QUEEN("Q", 1),
    KING("K", 1)
}

enum class Suit(val symbol: Char) {
    SPADES('♠'),
    HEARTS('♥'),
    DIAMONDS('♦'),
    CLUBS('♣')
}