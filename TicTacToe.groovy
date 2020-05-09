#!/usr/bin/env groovy
class Main {

	def input = new Scanner(System.in)

	static main(args) {
		Main main = new Main();
		main.play();
	}

	public void play() {

		TicTackToe game = new TicTackToe();
		game.init()
		def machine
		def gameOver = false
		def player = game.player1

		println "Choose who you are going to play with:\n"
		println "1: Machine			2: Player"

		try{
			machine = input.nextLine();
		} catch (Exception ex) {}

		println "Player 1 is X and Player 2 is O"
		println "${game.drawBoard()}"

		while (!gameOver && game.plays < 9) {

			player = game.currentPlayer == 1 ? game.player1 :game.player2
			def validPick = false;
			while (!validPick) {

				def square
				println "Next $player, enter :"

				if (machine == '2')
				{
					try {
						square = input.nextLine();
					} catch (Exception ex) { }
				} else {
					if (game.currentPlayer == 1) {
						try {
							square = input.nextLine();
						} catch (Exception ex) { }
					} else {
						Random random = new Random()
						square = random.nextInt(9)
						validPick = game.placeMarker(square)
						while (!validPick){
							square = random.nextInt(9)
							validPick = game.placeMarker(square)
						}
					}
				}

				if (machine == '2' || (machine == '1' && game.currentPlayer == 1)) {
					if (square.length() == 1 && Character.isDigit(square.toCharArray()[0])) {	validPick = game.placeMarker(square)	}
					if (!validPick) {	println "Select another Square"	}
				}

			}

			(game.checkWinner(player))?	gameOver = true	: game.switchPlayers()
			println(game.drawBoard());

		}
		println "Game Over, " + ((gameOver == true)? "$player Wins" : "Draw")
	}
}

public class TicTackToe {

	def board = new Object[3][3]
	def final player1 = "player 1"
	def final player2 = "player 2"
	def final markerO = 'O'
	def final markerX = 'X'

	int currentPlayer
	int plays

	public TicTacToe(){

	}

	def init() {
		int counter = 0;
		(0..2).each { row ->
			(0..2).each { col ->
				board[row][col] = (++counter).toString();
			}
		}
		plays = 0
		currentPlayer = 1
	}

	def switchPlayers() {
		currentPlayer = (currentPlayer == 1) ? 2 : 1
		plays++
	}

	def placeMarker(play) {
		def result = false
		(0..2).each { row ->
			(0..2).each { col ->
				if (board[row][col].toString() == play.toString()){
					board[row][col] = (currentPlayer == 1) ? markerX : markerO;
					result =  true;
				}
			}
		}
		return result;
	}

	def checkWinner(player) {
		char current = (player == player1)? markerX:  markerO
		//Checking
		return checkRows(current) || checkColumns(current) ||checkDiagonals(current);
	}

	def checkRows(char current){
		(0..2).any{ line ->
			board[line].every { it == current}
		}
	}


	def checkColumns(char current){
		(0..2).any{i ->
			(0..2).every{j ->
				board[j][i]==current }
		}
	}

	def checkDiagonals(char current){
		def rightDiag = [board[0][0],board[1][1],board[2][2]]
		def leftDiag =  [board[0][2],board[1][1],board[2][0]]
		return rightDiag.every{it == current} || leftDiag.every{it == current}
	}


	def drawBoard() {
		StringBuilder builder = new StringBuilder("\n === Board ===\n");
		(0..2).each { row->
			(0..2).each {col ->
				builder.append(" [" + board[row][col] + "] ");
			}
			if (row != 2) { builder.append("\n"); }
			builder.append("\n");
		}
		builder.append(" =============");
		builder.append("\n");
		return builder.toString();
	}
}
