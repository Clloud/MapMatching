# MapMatching
Implementation of Hidden Markov Models (HMM) for map matching problem according to the paper 
[*Hidden Markov map matching through noise and sparseness*](https://dl.acm.org/citation.cfm?doid=1653771.1653818&preflayout=flat#source). 
The test data is downloaded from [here](https://www.microsoft.com/en-us/research/publication/hidden-markov-map-matching-noise-sparseness/).

> **Abstract**
*The problem of matching measured latitude/longitude points to roads is becoming increasingly important. This paper 
describes a novel, principled map matching algorithm that uses a Hidden Markov Model (HMM) to find the most likely road 
route represented by a time-stamped sequence of latitude/longitude pairs. The HMM elegantly accounts for measurement 
noise and the layout of the road network. We test our algorithm on ground truth data collected from a GPS receiver in a 
vehicle. Our test shows how the algorithm breaks down as the sampling rate of the GPS is reduced. We also test the 
effect of increasing amounts of additional measurement noise in order to assess how well our algorithm could deal with 
the inaccuracies of other location measurement systems, such as those based on WiFi and cell tower multilateration. 
We provide our GPS data and road network representation as a standard test set for other researchers to use in their map
matching work.*