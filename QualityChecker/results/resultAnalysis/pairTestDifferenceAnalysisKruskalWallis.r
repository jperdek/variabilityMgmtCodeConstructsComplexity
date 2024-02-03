#install.packages('dplyr')
#install.packages('bestNormalize')
#install.packages('ggplot2')
#install.packages('easyGgplot2')
#install.packages('devtools')
library(dplyr)
library(bestNormalize)
library(devtools)
library(ggplot2)
library(broom)
library(stringr)
#devtools::install_github("kassambara/easyGgplot2")
library(easyGgplot2)

setwd("E:/aspects/spaProductLine/QualityChecker/results/resultAnalysis")
firstFormData <- read.csv('../forms/form1/classesTYPHONE_FORM1.csv', sep=";")
secondFormData <- read.csv('../forms/form2/classesTYPHONE_FORM2.csv', sep=";")
thirdFormData <- read.csv('../forms/form3/classesTYPHONE_FORM3.csv', sep=";")
fourthFormData <- read.csv('../forms/form4/classesTYPHONE_FORM4.csv', sep=";")
fifthFormData <- read.csv('../forms/form5/classesTYPHONE_FORM5.csv', sep=";")
secondFormData <- secondFormData[colnames(firstFormData)]
thirdFormData <- thirdFormData[colnames(firstFormData)]
fourthFormData <- fourthFormData[colnames(firstFormData)]
fifthFormData <- fifthFormData[colnames(firstFormData)]

print(colnames(effortDoubleFirstForm))
print(colnames(effortDoubleSecondForm))
#loadedDataWithDecorators <- filter(loadedData, Overall.file.decorastors > 0)
#firstFormDataWithDecorators <- filter(firstFormData, Overall.file.decorastors > 0)
#secondFormDataWithDecorators <- filter(firstFormData, Overall.file.decorastors > 0)
#fourthFormDataWithDecorators <- filter(fourthFormData, Overall.file.decorastors > 0)

#effortDoubleFirstForm <- as.double(unlist(lapply(firstFormDataWithDecorators$Halstead.Effort, function(a)  str_replace(a, ",", "."))))
#effortDoubleSecondForm <- as.double(unlist(lapply(secondFormDataWithDecorators$Halstead.Effort, function(a)  str_replace(a, ",", "."))))
#effortDoubleFourthForm <- as.double(unlist(lapply(fourthFormDataWithDecorators$Halstead.Effort, function(a)  str_replace(a, ",", "."))))



#print(cor(effortDoubleFirstForm, effortDoubleSecondForm, method = "spearman")) # PAIR TEST variables are correlated!!!
#print(cor(effortDoubleSecondForm, effortDoubleFourthForm, method = "spearman")) # PAIR TEST variables are correlated!!!
#print(cor(effortDoubleFirstForm, effortDoubleFourthForm, method = "spearman")) # PAIR TEST variables are correlated!!!

group <- rep("FORM1", length(firstFormData$Halstead.Effort))
effortDoubleFirstForm <- cbind(firstFormData, group)
group <- rep("FORM2", length(secondFormData$Halstead.Effort))
effortDoubleSecondForm <- cbind(secondFormData, group)
group <- rep("FORM3", length(secondFormData$Halstead.Effort))
effortDoubleThirdForm <- cbind(thirdFormData, group)
group <- rep("FORM4", length(fourthFormData$Halstead.Effort))
effortDoubleFourthForm <- cbind(fourthFormData, group)
group <- rep("FORM5", length(fourthFormData$Halstead.Effort))
effortDoubleFifthForm <- cbind(fifthFormData, group)

#http://www.sthda.com/english/wiki/kruskal-wallis-test-in-r

allEffort <<- rbind(effortDoubleFirstForm, effortDoubleSecondForm, effortDoubleThirdForm, effortDoubleFourthForm, effortDoubleFifthForm)
lapply(colnames(firstFormData), function(columnName, data) {
  allEffort[[columnName]] <<- as.double(unlist(lapply(allEffort[[columnName]], function(a)  str_replace(a, ",", "."))))
}, data=allEffort)
print(head(allEffort))
#allEffort$Halstead.Effort <- as.double(unlist(lapply(allEffort$Halstead.Effort, function(a)  str_replace(a, ",", "."))))
print(colnames(allEffort))
print(allEffort$Halstead.Effort)

pairwiseWilcoxTest <- pairwise.wilcox.test(allEffort$Halstead.Effort, allEffort$group,
                                                                                      p.adjust.method = "BH")
                                           print(pairwiseWilcoxTest)
                                           print(tidy(pairwiseWilcoxTest))
                                           
                                           
                                           
testColumnNames <- c('comparedName', 'p.value', 'chi.value', 'degreeOffreedom', 'comparison')
finalKrustalWillisTest <<- data.frame(matrix(nrow = 0, ncol = length(testColumnNames)))
colnames(finalKrustalWillisTest) <- testColumnNames
lapply(colnames(firstFormData), function(columnName, data) {
  print(columnName)
  if (length(na.omit(data[[columnName]])) == nrow(data)) {
    kruskalTest <- kruskal.test(as.formula(paste(columnName, " ~ group")), data = data)
    pairwiseWilcoxTest <- pairwise.wilcox.test(data[[columnName]], allEffort$group,
                                               p.adjust.method = "BH")
    finalKrustalWillisTest <<- rbind(finalKrustalWillisTest, c(columnName, kruskalTest$p.value, kruskalTest$statistic[["Kruskal-Wallis chi-squared"]],
                                                               kruskalTest$parameter[["df"]], do.call(paste, c(tidy(pairwiseWilcoxTest, sep = "")))))
  }
}, data=allEffort)
colnames(finalKrustalWillisTest) <- testColumnNames
print(finalKrustalWillisTest)

#allEffort$Halstead.Effort <- as.double(unlist(lapply(allEffort$Halstead.Effort, function(a)  str_replace(a, ",", "."))))
#kruskalTest <- kruskal.test(as.formula(paste("Halstead.Effort", " ~ group")), data = allEffort)
#print(kruskalTest$p.value)
#print(kruskalTest)
#print(kruskalTest$statistic[["Kruskal-Wallis chi-squared"]])
#print(kruskalTest$parameter[["df"]])

#pairwiseWilcoxTest <- pairwise.wilcox.test(allEffort$Halstead.Effort, allEffort$group,
#                                           p.adjust.method = "BH")
#print(pairwiseWilcoxTest)
#print(tidy(pairwiseWilcoxTest))

