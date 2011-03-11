-- List relations
select r.id, t1.external_id as 'from_topic', t2.external_id as 'to_topic', r.hop_count, r.weight from topic t1, topic t2, relation r where t1.id = r.start_topic_id and t2.id = r.end_topic_id order by from_topic, to_topic limit 10;

-- List relation paths of one relation
select r.id, re.position, t1.external_id as 'from_topic', t2.external_id as 'to_topic' from relation r, relation_path_element re, topic t1, topic t2, direct_relation dr where re.relation_id = r.id and dr.id = re.direct_relation_id and t1.id = dr.start_topic_id and t2.id = dr.end_topic_id and r.id = 4 order by position limit 100;