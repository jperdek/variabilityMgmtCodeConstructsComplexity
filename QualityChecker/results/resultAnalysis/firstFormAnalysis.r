#install.packages('dplyr')
#install.packages('bestNormalize')
#install.packages('ggplot2')
#install.packages('easyGgplot2')
#install.packages('devtools')
library(dplyr)
library(bestNormalize)
library(devtools)
library(ggplot2)
library(stringr)
#devtools::install_github("kassambara/easyGgplot2")
library(easyGgplot2)

setwd("E:/aspects/spaProductLine/QualityChecker/results/resultAnalysis")
loadedData <- read.csv('../forms/form1/classesTYPHONE_FORM1.csv', sep=";")

loadedDataWithDecorators <- filter(loadedData, Overall.file.decorastors > 0)
print(head(loadedDataWithDecorators))
print(colnames(loadedData))
print(nrow(loadedDataWithDecorators))
print(loadedData[['Overall.file.decorastors']])

effortDouble <- as.double(unlist(lapply(loadedDataWithDecorators$Halstead.Effort, function(a)  str_replace(a, ",", "."))))
numberDecorators <- as.double(unlist(lapply(loadedDataWithDecorators$Overall.file.decorastors, function(a)  str_replace(a, ",", "."))))

originalPriceNormalized <- unlist(lapply(seq_along(effortDouble), function(index) effortDouble[index] / as.double(numberDecorators[index])))
originalPriceNormalized <- effortDouble
print(numberDecorators)
print(effortDouble)
print(originalPriceNormalized)
print(effortDouble[1])
qqnorm(originalPriceNormalized,
       main = "Q-Q plot Halstead effor")
qqline(originalPriceNormalized,
       main = "Q-Q plot Halstead efort")
plot(originalPriceNormalized, xlab="Pôvodná cena", ylab="hodnoty", main="Density diagram pre znormalizovanú pôvodnú cenu")

plot(ggplot2.violinplot(data=originalPriceNormalized, xtitle="Violin plot pre pôvodnú cenu"), xlab="hodnoty", ylab="Pôvodná cena", main="Violin plot pre pôvodnú cenu")

#test normality
print(shapiro.test(originalPriceNormalized))