positiveCount <- 0
negativeCount <- 0
differences <- c(3,0,2,4,3,-4,1,-6,-1,1)
print(differences)
differencesUnorderedAbs <- unlist(lapply(differences, abs))
#effortDoubleDifferenceUnorderedAbs <- effortDoubleDifferenceUnorderedAbs[effortDoubleDifferenceUnorderedAbs > 0]
differencesUnorderedAbsOrderSequence <- order(differencesUnorderedAbs, decreasing = FALSE)
differencesUnorderedOrderSequence <- vector( "numeric", length(differencesOrderedAbs))
differencesOrderedAbs <- differencesUnorderedAbs[differencesUnorderedAbsOrderSequence]
print(differencesOrderedAbs)
print(differencesUnorderedAbs)
print(differencesUnorderedAbsOrderSequence)
differencesOrderValues <- vector( "numeric", length(differencesOrderedAbs))


index <- 2
skiped <- 0
while (index <= length(differencesOrderValues)) {
  if (differencesOrderedAbs[index - 1] == 0) {
    differencesUnorderedOrderSequence[differencesUnorderedAbsOrderSequence[index - 1]] = index - 1 - skiped
    index = index + 1;
    skiped <- skiped + 1
    next;
  }

  if (differencesOrderedAbs[index - 1] != differencesOrderedAbs[index]) {
    differencesOrderValues[index - 1] = index - 1 - skiped;
    differencesUnorderedOrderSequence[differencesUnorderedAbsOrderSequence[index - 1]] = index - 1 - skiped
    index = index + 1;
  } else {
    indexSum = 0;
    newIndex = index;
    numberFound = 0;
    while( newIndex <= length(differencesOrderValues) && 
           differencesOrderedAbs[newIndex - 1] == differencesOrderedAbs[newIndex]) {
      indexSum = newIndex - 1 + indexSum - skiped;
      numberFound = numberFound + 1;
      newIndex = newIndex + 1;
    }
    indexSum = newIndex - 1 + indexSum - skiped;
    print("All")
    print(indexSum)
    numberFound = numberFound + 1;
    calculatedNewValue = indexSum / numberFound;
    newIndex = index;
    while(newIndex <= length(differencesOrderValues)  && 
          differencesOrderedAbs[newIndex - 1] == differencesOrderedAbs[newIndex]) {
      differencesOrderValues[index - 1] = calculatedNewValue;
      differencesUnorderedOrderSequence[differencesUnorderedAbsOrderSequence[index - 1]] = calculatedNewValue;
      newIndex = newIndex + 1;
      index = index + 1;
    }
    differencesOrderValues[index - 1] = calculatedNewValue;
    differencesUnorderedOrderSequence[differencesUnorderedAbsOrderSequence[index - 1]] = calculatedNewValue;
    index = index + 1;
  }
}
differencesOrderValues[index - 1] <- index - 1 - skiped;
differencesUnorderedOrderSequence[differencesUnorderedAbsOrderSequence[index - 1]] = index - 1 - skiped;

positiveCount <<- 0
negativeCount <<- 0
lapply(seq_along(differences), function(index) {
  if (differences[index] > 0) {
    print(differencesUnorderedOrderSequence[index])
    positiveCount <<- positiveCount + differencesUnorderedOrderSequence[index];
  } else if (differences[index] < 0) {
    
    negativeCount <<- negativeCount + differencesUnorderedOrderSequence[index];
  }
})

print(positiveCount)
print(negativeCount)
print(differencesOrderValues)
minValue = min(positiveCount, negativeCount)