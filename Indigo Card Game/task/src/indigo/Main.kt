package indigo


fun main() {

    println("Indigo Card Game")

    while (true) {
        println("Play first?")
        when(readln()) {
            "yes" -> {
                playerHuman.isPlayerTurn = true
                playerHumanMovedFirst = true
                playerComputer.isPlayerTurn = false
                break
            }
            "no" -> {
                playerHuman.isPlayerTurn = false
                playerHumanMovedFirst = false
                playerComputer.isPlayerTurn = true
                break
            }
        }
    }
    play()
}

fun play() {
    shuffle()
    putInitialCardOnTheTable()
    playerHuman.getUpTo6Cards()
    playerComputer.getUpTo6Cards()
    while (!gameIsOver) {
        showTableCards()
        if (playerComputer.cardsInHandList.size == 0 && playerHuman.cardsInHandList.size == 0) {
            if (playerHumanWonLast!!) {
                while (tableCards.isNotEmpty()) {
                    playerHuman.cardsList.add(tableCards.removeLast())
                    playerHuman.score += playerHuman.cardsList[playerHuman.cardsList.lastIndex].rank.score
                }
            } else {
                while (tableCards.isNotEmpty()) {
                    playerComputer.cardsList.add(tableCards.removeLast())
                    playerComputer.score += playerComputer.cardsList[playerComputer.cardsList.lastIndex].rank.score
                }
            }
            addFinalScore()
            showScore()
            break
        }
        getMovingPlayer().chooseCardToPlay()
    }
    println("Game Over")
}

fun getMovingPlayer(): Player {
    return if (playerHuman.isPlayerTurn) {
        playerHuman
    } else playerComputer
}

fun putInitialCardOnTheTable() {
    repeat(4) {
        tableCards.add(deck.removeLast())
    }
    print("Initial cards on the table: ")
    tableCards.forEach { print("$it ") }
    println("\n")
}


fun showTableCards() {
    if (tableCards.isEmpty()) {
        println("No cards on the table")
    } else {
        println("${tableCards.size} cards on the table, and the top card is ${tableCards[tableCards.lastIndex]}")
    }
}

fun get() {
    println("Number of cards:")
    val number = readln().toIntOrNull()
    if (number !in 1..52 || number == null) {
        println("Invalid number of cards.")
    } else if (number > deck.size) {
        println("The remaining cards are insufficient to meet the request.")
    } else {
        repeat(number) {
            print("${deck.removeLast()} ")
        }
        println()
    }
}

fun shuffle() {
    deck = deck.shuffled().toMutableList()
}

fun reset() {
    deck = createDeck()
    println("Card deck is reset.")
}



