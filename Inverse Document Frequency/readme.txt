Goal:
------
Given such an input file, output the top 5
words in terms of IDF for each document 

Overview
--------

During natural language processing, we often want to separate the useful words
from the non-useful words. Non-useful words might be their extremely common
words, such as "the" or "if", or extremely rare words that are unique to some
documents. 

This can be done by only keeping the words with an IDF between a min
and a max value.

I will be choosing a min value of 2.0 and max value of 3.5

Task Description
----------------

In this problem, each document is a line in the input file
electronics_prod.sample200ul. Only words consisting of 3 or more letters (no
numbers or signs) will be considered.


I will be choosing a min value of 2.0 and max value of 3.5

The output can be written in a file.

Definitions
-----------

The IDF (inverse document frequency) is a measure of whether the word is common
or rare across all documents. It is obtained by dividing the total number of
documents by the number of documents containing the word, and then taking the
logarithm (base 10) of that quotient.

P.S.: 
For proper understanding I am attaching a Sample Input file and the output file.