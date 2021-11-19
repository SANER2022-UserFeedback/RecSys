# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.

"""
import pandas as pd
#import lightfm
import os
import numpy as np

from scipy.sparse import coo_matrix
from lightfm import LightFM
from lightfm.evaluation import precision_at_k, auc_score, recall_at_k
#import lightfm.cross_validation as eval
from collections import defaultdict
import collections



#import itertools




def get_freq_rating(df_columns, list_filtered, out_path):
    with open(out_path, 'w', encoding='utf-8', errors='ignore') as res:
        for col, freq in zip(df_columns,list_filtered):
            res.write(col+','+str(freq)+'\n')



def preprocess_repo_name(list_repos):
    preprocessed_list=[]
    for r in list_repos:
        preprocessed_list.append(str(r).replace('git://github.com/','').replace('/','__').replace('.git',''))
    return preprocessed_list

def get_crossrec_matrix(csv_file):

    df=pd.read_csv(csv_file, sep=';')
    
    return df

def get_crossrec_gt_projects(is_train):
    list_gt=[]

    if is_train:    
        for i in range (1,10):            
            for f in os.listdir('./CrossRec/experimental_results/CrossRec/Round'+str(i)+'/GroundTruth/'):
                list_gt.append(f)
    else:
        for f in os.listdir('./CrossRec/experimental_results/CrossRec/Round10/GroundTruth/'):
            list_gt.append(f)
    return list_gt        

#def get_test_projects(test_folder):
#    list_gt=[]    
#    for f in os.listdir('D:/repositoryGitHub/CrossRec/experimental_results/CrossRec/Round10/GroundTruth/'):
#        list_gt.append(f)
# 
#    return list_gt        
    


def get_crossrec_recommendations(is_train, cutoff):
    list_recs=[]
    if is_train:    
        for i in range (1,10):     
            for f in os.listdir('./CrossRec/experimental_results/CrossRec/Round'+str(i)+'/Recommendations/'):
                with open('./CrossRec/experimental_results/CrossRec/Round'+str(i)+'/Recommendations/'+f, 'r', encoding='utf-8', errors='ignore') as rec_file:
                    lines=rec_file.readlines()
                    #print(lines[:5])
                    for l in lines[:cutoff]:
                        splitted=l.split('\t')
                        list_recs.append(splitted[0].replace('#DEP#',''))
    else:
        for f in os.listdir('./CrossRec/experimental_results/CrossRec/Round10/Recommendations/'):
                with open('./CrossRec/experimental_results/CrossRec/Round10/Recommendations/'+f, 'r', encoding='utf-8', errors='ignore') as rec_file:
                    lines=rec_file.readlines()
                    #print(lines[:5])
                    for l in lines[:cutoff]:
                        splitted=l.split('\t')
                        list_recs.append(splitted[0].replace('#DEP#',''))                                             
                                 
    return list_recs

def get_freq_rec_items(list_recs, df_filtered):
    
    list_freqs=[]
    #list_recs=get_crossrec_recommendations(rec_path)    
    for rec in list_recs:               
        list_freqs.append(df_filtered[rec].sum())
        
    return list_freqs




def train_model(train, test):
    model = LightFM(learning_rate=0.02, loss='logistic')   
    
    
    model.fit(train, epochs=70)
    #print(test)
    train_precision = precision_at_k(model, train, k=5).mean()
    test_precision = precision_at_k(model, test, k=5).mean()
    train_recall = recall_at_k(model, train, k=5).mean()

    train_auc = auc_score(model, train).mean()
    
    test_auc = auc_score(model, test).mean()
    test_recall = recall_at_k(model, test, k=5).mean()
    
    print('train metrics ',train_precision, train_recall, train_auc)
    print('test metrics ',test_precision, test_recall, test_auc)    
    
def fit_model(train):
    model = LightFM(learning_rate=0.02, loss='warp')    
    model.fit(train, epochs=70)   
    return model

                
def map_test_to_ids(list_projects,list_recs, folder, cutoff):
    unique=set(list_recs) 
    lib_ids=np.arange(len(unique))   
    #libs
    unique=set(list_recs) 
    list_lib_freqs=get_freq_rec_items(unique)   
    dict_lib_ids=dict(zip(unique, lib_ids))    
    dict_lib_freq=dict(zip(lib_ids,list_lib_freqs))
    
    #projects
    proj_ids=np.arange(len(list_projects))
    dict_proj_ids=dict(zip(list_projects, proj_ids))    
    #x= 5
    list_ids_rec = []
    list_ids_proj = []
    
    for f in os.listdir(folder):
        list_ids_proj.append(dict_proj_ids.get(f))
        #print("project id ",dict_proj_ids.get(f))
        with open(folder+f, 'r', encoding='utf-8', errors='ignore') as rec_file:
            lines=rec_file.readlines()
            #print(lines[:5])
            for l in lines[:cutoff]:
                splitted=l.split('\t')
                lib=splitted[0].replace('#DEP#','')
                #print('lib id ', dict_lib_ids.get(lib))
                list_ids_rec.append(dict_lib_ids.get(lib))    
            
    
    
    row = np.repeat(list_ids_proj,cutoff)
    col = np.array(list_ids_rec)    
    list_ratings=[]
    for lib_id in list_ids_rec:
        list_ratings.append(dict_lib_freq.get(lib_id))
        
    data = np.array(list_ratings)
    return coo_matrix((data, (row, col)), shape=(len(row), len(col)))


#def map_train_to_ids(list_projects, list_recs, cutoff):
#    unique=set(list_recs) 
#    lib_ids=np.arange(len(unique))   
#    #libs
#    
#    list_lib_freqs=get_freq_rec_items(unique)   
#    dict_lib_ids=dict(zip(unique, lib_ids))    
#    dict_lib_freq=dict(zip(lib_ids,list_lib_freqs))    
#    #projects
#    
#    proj_ids=np.arange(len(list_projects))    
#    dict_proj_ids=dict(zip(list_projects, proj_ids))        
#    
#    #x= 5
#    list_ids_rec = []
#    list_ids_proj = []    
#    
#    for i in range(1,10):
#        for f in os.listdir('D:/repositoryGitHub/CrossRec/experimental_results/CrossRec/Round'+str(i)+'/Recommendations/'):
#                      
#                list_ids_proj.append(dict_proj_ids.get(f))                
#                #print("project id ",dict_proj_ids.get(f))
#                with open('D:/repositoryGitHub/CrossRec/experimental_results/CrossRec/Round'+str(i)+'/Recommendations/'+f, 'r', encoding='utf-8', errors='ignore') as rec_file:
#                    lines=rec_file.readlines()
#                    #print(lines[:5])
#                    for l in lines[:cutoff]:
#                        splitted=l.split('\t')
#                        lib=splitted[0].replace('#DEP#','')
#                        #print('lib id ', dict_lib_ids.get(lib))
#                        list_ids_rec.append(dict_lib_ids.get(lib))             
#    
#    
#    row = np.repeat(list_ids_proj,cutoff)
#    #print(row)    
#    col = np.array(list_ids_rec)       
#    #print(list_ids_rec)
#    list_ratings=[]
#    for lib_id in list_ids_rec:
#        list_ratings.append(dict_lib_freq.get(lib_id))        
#    data = np.array(list_ratings)    
#    return coo_matrix((data, (row, col)), shape=(len(row), len(col)))       

def map_train_to_ids(list_projects, list_recs, df):
    unique=set(list_recs) 
    lib_ids=np.arange(len(unique))     
    list_lib_freqs=get_freq_rec_items(unique,df)   
    dict_lib_ids=dict(zip(unique, lib_ids))    
    dict_lib_freq=dict(zip(lib_ids,list_lib_freqs))   
    
    proj_ids=np.arange(len(list_projects))    
    dict_proj_ids=dict(zip(list_projects, proj_ids))
    
    #reverse dicts
    reverse_dict_proj=dict(zip(proj_ids, list_projects))
    reverse_dict_lib= dict(zip(lib_ids,unique))
        
    return proj_ids, dict_proj_ids, lib_ids, dict_lib_ids, dict_lib_freq, reverse_dict_proj , reverse_dict_lib  


 
def build_coo_matrix(dict_proj_ids, dict_lib_ids,dict_lib_freq, cutoff):
    #x= 5
    list_ids_rec = []
    list_ids_proj = []    
    
    for i in range(1,10):
        for f in os.listdir('./CrossRec/experimental_results/CrossRec/Round'+str(i)+'/Recommendations/'):
                      
                list_ids_proj.append(dict_proj_ids.get(f))                
                #print("project id ",dict_proj_ids.get(f))
                
                with open('./CrossRec/experimental_results/CrossRec/Round'+str(i)+'/Recommendations/'+f, 'r', encoding='utf-8', errors='ignore') as rec_file:
                    lines=rec_file.readlines()                    
                    for l in lines[:cutoff]:
                        splitted=l.split('\t')
                        lib=splitted[0].replace('#DEP#','')
                        #print('lib id ', dict_lib_ids.get(lib))
                        list_ids_rec.append(dict_lib_ids.get(lib))             
                    
    
    row = np.repeat(list_ids_proj,cutoff)    
    col = np.array(list_ids_rec)       
    
    list_ratings=[]
    for lib_id in list_ids_rec:
        list_ratings.append(dict_lib_freq.get(lib_id))        
    data = np.array(list_ratings)    
    return coo_matrix((data, (row, col)), shape=(len(row), len(col)))
                


def get_test_libs(test_ratings):  

    dict_test_projects=test_ratings.todok()   
    
    temp_dict={}  
    
    for key, value in dict_test_projects.items():
        temp_dict.update({key[0]: key[1]})
    
    grouped_dict = defaultdict(list)
    
    for key, value in sorted(temp_dict.items()):
        grouped_dict[value].append(key)
        
#    for key, value in sorted(grouped_dict.items()):
#        print(key,value)
        
    return grouped_dict

def get_ranked_results(dict_test, map_lib, out_file):
    with open(out_file, 'w', encoding='utf-8', errors='ignore' ) as out:
        for proj_key in dict_test.keys():
            scores=model.predict(user_ids=proj_key, item_ids=dict_test.get(proj_key))
            out.write("project id "+str(reverse_proj.get(proj_key))+'\n')
            #print("project id ", reverse_proj.get(proj_key))
            out.write("libs rated "+ str(get_lib_name(dict_test,proj_key,map_lib))+'\n')
            out.write("ranking "+ str(np.argsort(-scores))+'\n')
            #print("libs rated ", get_lib_name(dict_test,proj_key,map_lib))
            #print("ranking ", np.argsort(-scores))   



def preprocess_crossrec_lib(string):
     splitted=string.split('\t')
     lib=splitted[0].replace('#DEP#','')
     return lib
    




def get_ranked_recommendations(model, path_test, dict_proj, proj , dict_lib, cutoff , df_train, out_file, string_lib):
    list_libs = []
    
    
    with open(out_file, 'a', encoding='utf8', errors='ignore') as res_file:        
        ranked_dict = {}
        with open(path_test, 'r', encoding='utf8', errors='ignore') as test_file:
            proj_id=dict_proj.get(proj)
            lines = test_file.readlines()
            
            for l in lines[:cutoff]:
                lib=preprocess_crossrec_lib(l)
                #print('lib id ', dict_lib_ids.get(lib))
                list_libs.append(dict_lib.get(lib))  
            scores=model.predict(user_ids=proj_id, item_ids=list_libs)
            
            
            results_list=list(preprocess_crossrec_lib(l) for l in lines[:cutoff])
            ranks=np.argsort(-scores)
            if string_lib in results_list:
                res_file.write( str(results_list.index(string_lib))+',')
#            print("project id "+str(proj))
#            print("libs rated "+ str(results_list)+'\n')
#            print("ranking "+ str(ranks)+'\n')
            
            
#            res_file.write("project id "+str(proj)+ '\n')
#            res_file.write("crossrec ranking: \n")
            
            
#            for r in results_list:                
#                res_file.write(str(r)+'\n')
            #res_file.write("ranking "+ str(ranks)+'\n')
            
            for score, lib in zip(ranks, results_list):
                ranked_dict.update({score:lib})
                
                
            ranked_items= collections.OrderedDict(sorted(ranked_dict.items()))
            #sliced_rank= dict(itertools.islice(ranked_items.items(), 5))
            for key, value in ranked_items.items():
                if value == string_lib:
                    res_file.write(str(key)+ '\n')
                    #print('popularity rate '+ str(df_train[value].sum()))
                
#                res_file.write(str(key)+ ' '+str(value)+ '\n')
#                res_file.write('popularity ranking ' + str(df_train[value].sum())+'\n')   
                
                
                
            #res_file.write('='*300+ '\n')    
            
            
        return 
        
        
def get_lib_name(dict_rec,key, map_lib):
    list_libs=[]
    list_to_map = dict_rec.get(key)
    for l in list_to_map:
        list_libs.append(map_lib.get(l))
    
    return list_libs



## parameter settings   

ranks=[1200,20,40,100,200,600,1000]

tot=1200


cutoff=10
lib_folder='junit'      

results_folder='./results_LTR/results_rank_negative/cutoff_'+str(cutoff)+'/'+lib_folder+'/'   
testing_libs=['org.apache.avro:avro', 'com.google.guava:guava', 'com.google.inject:guice','junit:junit', 'log4j:log4j', 'org.mockito:mockito-core', 'org.slf4j:slf4j-api', 'org.springframework:spring-web']


for rank in ranks:
    
    lib_to_check=testing_libs[3]
    print(lib_to_check)
    print(cutoff)
    
    csv_path='./crossrec_data.csv'
    results_path='.results_LTR/results_rank_negative/cutoff_'+str(cutoff)+'/'+lib_folder+'/rank_comparison_'+str(rank)+'.csv'
    
    train_list=get_crossrec_gt_projects(True)  
    df_crossrec=get_crossrec_matrix(csv_path)  

    
    
    list_repo_name=preprocess_repo_name(df_crossrec['REPO'])    
    
    df_crossrec['REPO'] = list_repo_name
    
    positive_ratings=np.ones(rank, dtype='int')
    negative_ratings = np.negative(positive_ratings)
    
    zero_ratings=np.zeros(tot-rank, dtype='int')
    
    mutated_ratings= np.concatenate((negative_ratings, zero_ratings), axis=None)
    df_crossrec[lib_to_check]= mutated_ratings
    
        
        
    df_train=df_crossrec
    train_recs=get_crossrec_recommendations(True,cutoff) 
    
    
    
    id_projects, map_projects, id_libs, map_lib, lib_ratings, reverse_proj, reverse_lib =map_train_to_ids(df_train['REPO'], train_recs, df_train)
    train_rating_matrix= build_coo_matrix(map_projects, map_lib, lib_ratings, cutoff)
    
    

    
    
    model=fit_model(train_rating_matrix)
    
    
    #get_ranked_results(dict_test, reverse_lib, results_path)
    
    for i in range(1,10):
        test_path = './CrossRec/experimental_results/CrossRec/Round'+str(i)+'/Recommendations/'
        
        for test_file in os.listdir(test_path):      
    
            ranked_items=get_ranked_recommendations(model, test_path+test_file, map_projects,test_file, map_lib, cutoff, df_train, results_path, lib_to_check)   
    #        for key, value in ranked_items.items():
    #            print(key,value)
    #            print('popularity ranking', df_train[value].sum())
    #     
    
    
    
    print("file rank "+ str(rank) + ' created for '+ lib_to_check)
    
    
def evaluate_ranks_positive(folder): 
    
    df_rank_0=pd.read_csv(folder+'rank_comparison_0.csv', names=['crossrec_rate','rate_0'])      
    df_rank_0['rate_20'] = pd.read_csv(folder+'rank_comparison_20.csv',names=['crossrec_rate','rate_20']).iloc[:,-1]
    df_rank_0['rate_40'] = pd.read_csv(folder+'rank_comparison_40.csv',names=['crossrec_rate','rate_40']).iloc[:,-1]
    df_rank_0['rate_100'] = pd.read_csv(folder+'rank_comparison_100.csv',names=['crossrec_rate','rate_100']).iloc[:,-1]
    df_rank_0['rate_200'] = pd.read_csv(folder+'rank_comparison_200.csv',names=['crossrec_rate','rate_200']).iloc[:,-1]
    df_rank_0['rate_600'] = pd.read_csv(folder+'rank_comparison_600.csv',names=['crossrec_rate','rate_600']).iloc[:,-1]
    df_rank_0['rate_1000'] = pd.read_csv(folder+'rank_comparison_1000.csv',names=['crossrec_rate','rate_1000']).iloc[:,-1]
    
    
    df_rank_0.loc[df_rank_0['rate_0'] > df_rank_0['rate_20'], 'results_20'] = '1'
    df_rank_0.loc[df_rank_0['rate_0'] <= df_rank_0['rate_20'], 'results_20'] = '0' 
    
    df_rank_0.loc[df_rank_0['rate_0'] > df_rank_0['rate_40'], 'results_40'] = '1'
    df_rank_0.loc[df_rank_0['rate_0'] <= df_rank_0['rate_40'], 'results_40'] = '0' 
    
    df_rank_0.loc[df_rank_0['rate_0'] > df_rank_0['rate_100'], 'results_100'] = '1'
    df_rank_0.loc[df_rank_0['rate_0'] <= df_rank_0['rate_100'], 'results_100'] = '0' 
    
    df_rank_0.loc[df_rank_0['rate_0'] > df_rank_0['rate_200'], 'results_200'] = '1'
    df_rank_0.loc[df_rank_0['rate_0'] <= df_rank_0['rate_200'], 'results_200'] = '0'
    
    df_rank_0.loc[df_rank_0['rate_0'] > df_rank_0['rate_600'], 'results_600'] = '1'
    df_rank_0.loc[df_rank_0['rate_0'] <= df_rank_0['rate_600'], 'results_600'] = '0' 
    
    df_rank_0.loc[df_rank_0['rate_0'] > df_rank_0['rate_1000'], 'results_1000'] = '1'
    df_rank_0.loc[df_rank_0['rate_0'] <= df_rank_0['rate_1000'], 'results_1000'] = '0' 
    
    #print(df_rank_0.iloc[:,8:].sum()/df_rank_0.shape[0])
    df_rank_0.to_csv(folder+'merged_results.csv', index=False)
    
def evaluate_ranks_negative(folder): 
    
    df_rank_0=pd.read_csv(folder+'rank_comparison_1200.csv', names=['crossrec_rate','rate_1200'])      
    df_rank_0['rate_20'] = pd.read_csv(folder+'rank_comparison_20.csv',names=['crossrec_rate','rate_20']).iloc[:,-1]
    df_rank_0['rate_40'] = pd.read_csv(folder+'rank_comparison_40.csv',names=['crossrec_rate','rate_40']).iloc[:,-1]
    df_rank_0['rate_100'] = pd.read_csv(folder+'rank_comparison_100.csv',names=['crossrec_rate','rate_100']).iloc[:,-1]
    df_rank_0['rate_200'] = pd.read_csv(folder+'rank_comparison_200.csv',names=['crossrec_rate','rate_200']).iloc[:,-1]
    df_rank_0['rate_600'] = pd.read_csv(folder+'rank_comparison_600.csv',names=['crossrec_rate','rate_600']).iloc[:,-1]
    df_rank_0['rate_1000'] = pd.read_csv(folder+'rank_comparison_1000.csv',names=['crossrec_rate','rate_1000']).iloc[:,-1]
    
    
    
    df_rank_0.loc[df_rank_0['rate_1200'] >= df_rank_0['rate_1000'], 'results_1000'] = '0'
    df_rank_0.loc[df_rank_0['rate_1200'] < df_rank_0['rate_1000'], 'results_1000'] = '1' 
    
    
    df_rank_0.loc[df_rank_0['rate_1200'] >= df_rank_0['rate_600'], 'results_600'] = '0'
    df_rank_0.loc[df_rank_0['rate_1200'] < df_rank_0['rate_600'], 'results_600'] = '1' 
    
    df_rank_0.loc[df_rank_0['rate_1200'] >= df_rank_0['rate_200'], 'results_200'] = '0'
    df_rank_0.loc[df_rank_0['rate_1200'] < df_rank_0['rate_200'], 'results_200'] = '1' 
    
    df_rank_0.loc[df_rank_0['rate_1200'] >= df_rank_0['rate_100'], 'results_100'] = '0'
    df_rank_0.loc[df_rank_0['rate_1200'] < df_rank_0['rate_100'], 'results_100'] = '1' 
    
    df_rank_0.loc[df_rank_0['rate_1200'] >= df_rank_0['rate_40'], 'results_40'] = '0'
    df_rank_0.loc[df_rank_0['rate_1200'] < df_rank_0['rate_40'], 'results_40'] = '1'
    
    df_rank_0.loc[df_rank_0['rate_1200'] >= df_rank_0['rate_20'], 'results_20'] = '0'
    df_rank_0.loc[df_rank_0['rate_1200'] < df_rank_0['rate_20'], 'results_20'] = '1' 
    
   
    
    #print(df_rank_0.iloc[:,8:].sum()/df_rank_0.shape[0])
    df_rank_0.to_csv(folder+'merged_results_negative.csv', index=False)
    
#    
#    
#    
#    #list_dfs=[df_rank_0, df_rank_20,df_rank_40,df_rank_60,df_rank_200,df_rank_600,df_rank_1000]
#    
#    df_rank_0.reset_index()
#
#    merged_df=pd.merge(df_rank_0, df_rank_20, how='outer', on='crossrec_rate')
##    merged_df_part2=pd.merge(merged_df_part1, df_rank_40, how='left', on='crossrec_rate')
##    merged_df_part3=pd.merge(merged_df_part2, df_rank_60, how='left', on='crossrec_rate')
#    print(df_rank_0.shape)
#    print(merged_df.shape)
    
def compute_metrics(csv_results):

    df_results=pd.read_csv(csv_results)
    print(df_results.iloc[:,8:].sum()/df_results.shape[0])
    
    print(df_results['rate_1200'].mean())
    print(df_results.shape[0]/1180)
        

#
cutoff=20
lib_folder='junit'            
#evaluate_ranks_negative(results_folder)
compute_metrics('C:/Users/claudio/Desktop/Spyder_folder/results_rank_negative/cutoff_'+
                str(cutoff)+'/'+lib_folder+'/merged_results_negative.csv')                            
    
    


