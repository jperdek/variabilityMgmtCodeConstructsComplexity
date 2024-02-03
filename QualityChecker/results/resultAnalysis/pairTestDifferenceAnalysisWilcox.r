#install.packages('dplyr')
#install.packages('bestNormalize')
#install.packages('ggplot2')
#install.packages('easyGgplot2')
#install.packages('devtools')
#install.packages('data.table')
library(dplyr)
library(bestNormalize)
library(devtools)
library(ggplot2)
library(broom)
library(stringr)
library(data.table)
#devtools::install_github("kassambara/easyGgplot2")
library(easyGgplot2)

setwd("E:/aspects/spaProductLine/QualityChecker/results/resultAnalysis")
firstFormData <<- read.csv('../forms/form1/classesTYPHONE_FORM1.csv', sep=";")
secondFormData <<- read.csv('../forms/form2/classesTYPHONE_FORM2.csv', sep=";")
thirdFormData <<- read.csv('../forms/form3/classesTYPHONE_FORM3.csv', sep=";")
fourthFormData <<- read.csv('../forms/form4/classesTYPHONE_FORM4.csv', sep=";")
fifthFormData <<- read.csv('../forms/form5/classesTYPHONE_FORM5.csv', sep=";")

firstFormData <- filter(firstFormData, Overall.file.decorastors > 0)
secondFormData <- filter(secondFormData, Overall.file.decorastors > 0)
thirdFormData <- filter(thirdFormData, Overall.file.decorastors > 0)
fourthFormData <- filter(fourthFormData, Overall.file.decorastors > 0)
fifthFormData <- filter(fifthFormData, Overall.file.decorastors > 0)

secondFormData <<- secondFormData[colnames(firstFormData)]
thirdFormData <<- thirdFormData[colnames(firstFormData)]
fourthFormData <<- fourthFormData[colnames(firstFormData)]
fifthFormData <<- fifthFormData[colnames(firstFormData)]

lapply(colnames(firstFormData), function(columnName, data) {
  firstFormData[[columnName]] <<- as.double(unlist(lapply(firstFormData[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=firstFormData)
lapply(colnames(secondFormData), function(columnName, data) {
  secondFormData[[columnName]] <<- as.double(unlist(lapply(secondFormData[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=secondFormData)
lapply(colnames(thirdFormData), function(columnName, data) {
  thirdFormData[[columnName]] <<- as.double(unlist(lapply(thirdFormData[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=thirdFormData)
lapply(colnames(fourthFormData), function(columnName, data) {
  fourthFormData[[columnName]] <<- as.double(unlist(lapply(fourthFormData[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=fourthFormData)
lapply(colnames(fifthFormData), function(columnName, data) {
  fifthFormData[[columnName]] <<- as.double(unlist(lapply(fifthFormData[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=fifthFormData)



testNormalityColumnNames <- c("Form", "Complexity.name", "Statistics", "p.value")
normalityTest <<- data.frame(matrix(nrow = 0, ncol = length(testNormalityColumnNames)))

drawQQPlots <- function(formData, formName) {
  par(mfrow=c(2, 2))
  lapply(colnames(firstFormData), function(columnName, data1) {
    if (length(na.omit(data1[[columnName]])) == nrow(data1)) {
      qqnorm(data1[[columnName]], main = str_replace_all(paste("Q-Q plot ", columnName), '[.]', " "))
      qqline(data1[[columnName]], main = str_replace_all(paste("Q-Q plot ", columnName), '[.]', " "))
      shapiroTest <- shapiro.test(data1[[columnName]])
      normalityTest <<- rbind(normalityTest, c(formName, columnName, shapiroTest[["statistic"]]["W"], shapiroTest[["p.value"]]))
      
    }
  }, data1=formData)
}

drawQQPlots(firstFormData, 1)
drawQQPlots(secondFormData, 2)
drawQQPlots(thirdFormData, 3)
drawQQPlots(fourthFormData, 4)
drawQQPlots(fifthFormData, 5)

colnames(normalityTest) <- testNormalityColumnNames
print(normalityTest)
write.table(normalityTest, ".\\normalityTest.csv", row.names=FALSE, dec=",", sep=";")


ifPaired <- TRUE
chosenAlternative <- c("two.sided", "less", "greater")
testColumnNames <- c('comparedName', 'correlation', 'statistics W', 'p.value', 'null.value', 'alternative', 'method', 'data.name', 'confidence.interval.start', 'confidence.interval.end', 'estimate')
finalWilcoxTest <<- data.frame(matrix(nrow = 0, ncol = length(testColumnNames)))
colnames(finalWilcoxTest) <- testColumnNames

#firstFormData <- firstFormData[,unlist(lapply(firstFormData,is.numeric))]
#secondFormData <- secondFormData[,unlist(lapply(secondFormData,is.numeric))]
lapply(colnames(firstFormData), function(columnName, data1, data2) {
  if (length(na.omit(data1[[columnName]])) == nrow(data1)) {
    correlation <- cor(data1[[columnName]], data2[[columnName]], method = "spearman")
    wilcoxTest <- wilcox.test(data1[[columnName]], data2[[columnName]], alternative=chosenAlternative, paired=ifPaired, exact = TRUE, correct = TRUE,
                              conf.int = TRUE, conf.level = 0.95)
    print(columnName)
    print(wilcoxTest)
    if (ifPaired) {
      confInt <- as.vector(wilcoxTest$conf.int)
      finalWilcoxTest <<- rbind(finalWilcoxTest, c(columnName, as.double(correlation), wilcoxTest$statistic,
                                                   as.double(wilcoxTest$p.value), as.double(wilcoxTest$null.value), wilcoxTest$alternative, 
                                                                 wilcoxTest$method, wilcoxTest$data.name, as.double(confInt[1]), as.double(confInt[2]), as.double(wilcoxTest$estimate )))
    } else {
      confInt <- as.vector(wilcoxTest$conf.int)
      finalWilcoxTest <<- rbind(finalWilcoxTest, c(columnName, as.double(correlation), as.double(wilcoxTest$statistic[["W"]]),
                                                   as.double(wilcoxTest$p.value), as.double(wilcoxTest$null.value), wilcoxTest$alternative, 
                                                               wilcoxTest$method, wilcoxTest$data.name, as.double(confInt[1]), as.double(confInt[2]), as.double(wilcoxTest$estimate )))
    }
    
  } else {
    finalWilcoxTest <<- rbind(finalWilcoxTest, c(columnName, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1))
  }
}, data1=firstFormData, data2=secondFormData)
colnames(finalWilcoxTest) <- testColumnNames
print(finalWilcoxTest)

write.table(finalWilcoxTest, ".\\pairedTestForm1-2.csv", row.names=FALSE, dec=",", sep=";")

