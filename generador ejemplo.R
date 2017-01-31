library(igraph)
library(methods)
library(optrees)
library(cooptrees)

for(cont in 1:10)
{n<-4
dens<- 0.8
T<-100
R<-10

nombre<-paste("ejemplo","_",n+1,"_",dens*100,"_",T,"_",cont,".dat",sep="")

x<-runif(2*n,2,8) 
x<-c(c(0,0),x)
n<-n+1
v<-matrix(x,nrow=n,byrow=T)
s<-c(1:n^2)
d<-matrix(s,nrow=n)
for(i in 1:n)
for(j in 1:n)
d[i,j]<-sqrt((v[i,1]-v[j,1])^2+(v[i,2]-v[j,2])^2)
d<-trunc(d)+2
for(i in 1:n)
d[i,i]<-0
nodes<-1:n
r=round(n*(n-1)/2)
l<-c(1:r)
e=r*3
z<-c(1:e)
completo<-matrix(z,nrow=r)
tercer<-l
k<-1
for(i in 1:n)
{for(j in 1:n)
{if(j>i)
{tercer[k]<-d[i,j]
completo[k,]<-c(i,j,d[i,j])
k<-k+1}}
k}
graf<-msTreePrim(nodes,completo)$tree.arcs
aristas<-round(dens*r)+1
graf<-matrix(graf,ncol=3)

while(nrow(graf)<aristas) 
{m<-0
f1<-trunc(runif(1)*r)+1
for(i in 1:nrow(graf))
if (identical(completo[f1,1:2],graf[i,1:2])|identical(rev(completo[f1,1:2]),graf[i,1:2]))
{m<-m+1}

if(m==0)
graf<-rbind(graf,completo[f1,])
}

repGraph (nodes,graf)
w<-c(1:n)
suma<-0
for(i in 1:(n-1))
{w[i+1]<-trunc(runif(1)*5)+1
suma<-suma+w[i+1]
}
w[1]<-round(suma/(n-1))
w[1]<-0
w<-matrix(w)

o<-3
M<-3


s<-matrix(c("P=",n,";","T=",T,";","R=",R,";","M=",M,";","v=",o,";"),ncol=3,byrow=T)
write.table(s, file = nombre, append = TRUE, quote = FALSE, sep = " ", eol = "\n", na = "NA", dec = ".",
row.names = FALSE, col.names = FALSE, qmethod = c("escape", "double"))
tasa<-"r=["
tasa<-paste(tasa,w[1],sep="")
for(i in 2:n)
{tasa<-paste(tasa,w[i],sep=",")
}
tasa<-paste(tasa,"];",sep="")
write.table(tasa, file = nombre, append = TRUE, quote = FALSE, sep = " ", eol = "\n", na = "NA", dec = ".",
row.names = FALSE, col.names = FALSE, qmethod = c("escape", "double"))


graf1<-ArcList2Cmat (nodes , graf , directed = FALSE)
t<-rep(0,n^2)
t<-matrix(t,ncol=n)

for(i in 1:n)
{for(j in 1:n){
if(i!=j)
{if(graf1[i,j]<100000)
t[i,j]<-graf1[i,j]
}
}
}

imprimir=function(A,g)
{imp<-paste(g,"=[[")
for(i in 1:n)
{for(j in 1:n)
{if(j!=n)
imp<-c(imp,A[i,j],",")  
else
imp<-c(imp,A[i,j]) 
}
if(i!=n)
imp<-c(imp,"],")
else
imp<-c(imp,"],];")

if( i!=n)
imp<-c(imp,"[")
}
imp<-matrix(imp,ncol=(2*n+1),byrow=T)
imp
}


write.table(imprimir(d,"d"), file = nombre, append = TRUE, quote = FALSE, sep = " ", eol = "\n", na = "NA", dec = ".",
row.names = FALSE, col.names = FALSE, qmethod = c("escape", "double"))

write.table(imprimir(t,"t"), file = nombre, append = TRUE, quote = FALSE, sep = " ", eol = "\n", na = "NA", dec = ".",
row.names = FALSE, col.names = FALSE, qmethod = c("escape", "double"))

interf<-d

for(i in 1:n)
for(j in 1:n)
{if (i==j)
interf[i,j]<-100
else
interf[i,j]<-10
}
write.table(imprimir(interf,"alpha"), file = nombre, append = TRUE, quote = FALSE, sep = " ", eol = "\n", na = "NA", dec = ".",
row.names = FALSE, col.names = FALSE, qmethod = c("escape", "double"))

wir<-d

for(i in 1:n)
for(j in 1:n)
{if (d[i,j]<5)
wir[i,j]<-1
else
wir[i,j]<-0
}

write.table(imprimir(wir,"wir"), file = nombre, append = TRUE, quote = FALSE, sep = " ", eol = "\n", na = "NA", dec = ".",
row.names = FALSE, col.names = FALSE, qmethod = c("escape", "double"))
}




