import scala.collection.immutable.HashSet
import scala.util.Random
import scala.util.control.Breaks._

object Main {
    def main(args: Array[String]): Unit = {
		welcome_message()
		game_loop()
		goodbye_message()
	}

	def choose_player(): Integer = {
		println("Choose who you are going to play with")
		println("1: Machine		2: Player")
		val player = scala.io.StdIn.readInt()
		val machine = if (player == 1) 1 else 0
		return machine
	}

	def print_board(choose: Integer, player: Integer, board: List[Char]): List[Char] = {
		val play1 = 'x'
		val play2 = 'o'
		val step = if (player == 1) play1 else play2
		val new_board = board.updated(choose - 1, step)

		new_board.grouped(3).foreach(row => println(row(0) + " " + row(1) + " " + row(2)))
		return new_board
	}

	def goodbye_message(){
		println("Hope see you soon.")
	}

	def game_loop(){
		var grid: HashSet[Integer] = HashSet(1, 2, 3, 4, 5, 6, 7, 8, 9)
		var board = ('1' to '9').toList
		var player1: List[Integer] = List()
		var player2: List[Integer] = List()
		var round = 0
		var winner = 0
		var machine = 0

		breakable {	
			machine = choose_player()
			while(true)
			{
				var choose = 0

				//turn player 1
				round += 1
				if (round == 10) { break }
				choose = turn(grid, "Player 1")
				player1 = player1 :+ choose
				board = print_board(choose, 1, board)
				grid = grid - choose

				if (round >= 5) { winner = check(player1, 1) }
				if (winner != 0) { break }

				//turn player 2
				round += 1
				if (round == 10) { break }
				if (machine == 0){
					choose = turn(grid, "Player 2")
				} else {
					choose = Machine(grid)
				}
				board = print_board(choose, 2, board)
				player2 = player2 :+ choose
				grid = grid - choose

				if (round >= 5) { winner = check(player2, 2) }
				if (winner != 0) { break }
			}
		}
		if (winner != 0) { 
			if (winner == 2 && machine == 1){
				println("Machine has won")
			} else {
				println(s"Player${winner} has won")
			}
		}
		else { print("Tie!! ") }
	}

	def check(player: List[Integer], player_number: Integer): Integer = {
		var winner = 0
		val player_hash = HashSet[Integer]() ++ player
		var hardcode_winning: List[HashSet[Integer]] = List(
			HashSet(1, 2, 3),
			HashSet(4, 5, 6),
			HashSet(7, 8, 9),
			HashSet(1, 4, 7),
			HashSet(2, 5, 8),
			HashSet(3, 6, 9),
			HashSet(1, 5, 9),
			HashSet(3, 5, 7)
		)
		for (win <- hardcode_winning) {
			val intersection = player_hash & win
			if (win.equals(intersection)) { winner = player_number}
		}
		
		return winner
	}

	def Machine(grid: HashSet[Integer]): Integer = {
		println(s"Machine, Choose a sector:")
		var GenRandom = new Random()
		var choose = GenRandom.nextInt(9)
		while (! grid(choose)){
			choose = GenRandom.nextInt(9)
		}
		return choose
	}

	def turn(grid: HashSet[Integer], player: String): Integer = {
		// Turno
		println(s"${player}, Choose a sector:")
		var choose = 0
		var correct_input = false
		while (!correct_input){
			choose = scala.io.StdIn.readInt()

			if (choose > 9) { println("choose a numbe between 1 - 9") }
			else if (! grid(choose)) { println("that sector is not available") }
			else { correct_input = true; return choose}
		}
		return choose
	}

	def welcome_message() {
		println("Welcome to this Tick Tack Toe game implementation in scala!")
	}
}
