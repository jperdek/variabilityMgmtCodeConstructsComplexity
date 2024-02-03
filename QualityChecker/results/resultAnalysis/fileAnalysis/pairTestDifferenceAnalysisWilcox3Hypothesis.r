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
firstFormData <<- read.csv('../forms/form1/generalAGGREGATETYPHONE_FORM1.csv', sep=";")
thirdFormData <<- read.csv('../forms/form3/generalAGGREGATETYPHONE_FORM3.csv', sep=";")

firstFormData <- filter(firstFormData, Overall.file.decorastors > 0)
thirdFormData <- filter(thirdFormData, Overall.file.decorastors > 0)
thirdFormData <<- thirdFormData[colnames(firstFormData)]


lapply(colnames(firstFormData), function(columnName, data) {
  firstFormData[[columnName]] <<- as.double(unlist(lapply(firstFormData[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=firstFormData)
lapply(colnames(thirdFormData), function(columnName, data) {
  thirdFormData[[columnName]] <<- as.double(unlist(lapply(thirdFormData[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=thirdFormData)


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
}, data1=thirdFormData, data2=firstFormData)
colnames(finalWilcoxTest) <- testColumnNames
print(finalWilcoxTest)

write.table(finalWilcoxTest, ".\\fileAnalysis\\pairedTestForm3-1.csv", row.names=FALSE, dec=",", sep=";")

