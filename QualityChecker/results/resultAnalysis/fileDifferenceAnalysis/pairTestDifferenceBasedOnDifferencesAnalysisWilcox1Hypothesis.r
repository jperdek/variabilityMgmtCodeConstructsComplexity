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
secondScenarioData <<- read.csv('../scenario2/generalAGGREGATETYPHONE.csv', sep=";")
fifthScenarioData <<- read.csv('../scenario5/generalAGGREGATETYPHONE.csv', sep=";")


secondScenarioData <- secondScenarioData[which(firstFormData$Overall.file.decorastors > 0),]
fifthScenarioData <- fifthScenarioData[which(firstFormData$Overall.file.decorastors > 0),]
firstFormData <- filter(firstFormData, Overall.file.decorastors > 0)

print("DONE")
fifthScenarioData <<- fifthScenarioData[colnames(secondScenarioData)]

lapply(colnames(firstFormData), function(columnName, data) {
  firstFormData[[columnName]] <<- as.double(unlist(lapply(firstFormData[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=firstFormData)
lapply(colnames(secondScenarioData), function(columnName, data) {
  secondScenarioData[[columnName]] <<- as.double(unlist(lapply(secondScenarioData[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=secondScenarioData)
lapply(colnames(fifthScenarioData), function(columnName, data) {
  fifthScenarioData[[columnName]] <<- as.double(unlist(lapply(fifthScenarioData[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=fifthScenarioData)




testNormalityColumnNames <- c("Form", "Complexity.name", "Statistics", "p.value")
normalityTest <<- data.frame(matrix(nrow = 0, ncol = length(testNormalityColumnNames)))

drawQQPlots <- function(formData, formName) {
  par(mfrow=c(2, 2))
  lapply(colnames(firstFormData), function(columnName, data1) {
    if (length(na.omit(data1[[columnName]])) == nrow(data1)) {
      qqnorm(data1[[columnName]], main = str_replace_all(paste("Q-Q plot ", columnName), '[.]', " "))
      qqline(data1[[columnName]], main = str_replace_all(paste("Q-Q plot ", columnName), '[.]', " "))
      tryCatch( {
      shapiroTest <- shapiro.test(data1[[columnName]])
      normalityTest <<- rbind(normalityTest, c(formName, columnName, shapiroTest[["statistic"]]["W"], shapiroTest[["p.value"]]))
    }, error=function(cond) {},finally={})
    }
  }, data1=formData)
}

drawQQPlots(firstFormData, 1)
drawQQPlots(secondScenarioData, 2)
drawQQPlots(fifthScenarioData, 3)


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
}, data1=secondScenarioData, data2=fifthScenarioData)
colnames(finalWilcoxTest) <- testColumnNames
print(finalWilcoxTest)

write.table(finalWilcoxTest, ".\\fileDifferenceAnalysis\\scenario2-5ComplexityOfForms.csv", row.names=FALSE, dec=",", sep=";")

