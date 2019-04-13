//-------------------------------------------------------------------------
// musicMarkov.java
//
// Takes input of parsed midi input and arranges into markov chains that represent
// a probabilistic copy of the music of the given input.
//
// Alexander Lill and Tyler Batistic
// HackACM 2019
// 4/13/19
//-------------------------------------------------------------------------

class musicMarkov {
	// Fields
	double[][] notes;
	double[][] length;

	musicMarkov(Object A) {

